package com.project.p_project.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.project.p_project.model.security.dto.RegistrationRequest;
import com.project.p_project.model.security.dto.AuthRequest;
import com.project.p_project.security.JwtAuthenticationProvider;
import com.project.p_project.service.user.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@Api(value = "Работа с авторизацией и регистрацией",tags = "Authorization")
public class AuthResourceController {
    private final UserService userService;
    private final JwtAuthenticationProvider authenticationProvider;

    @Autowired
    public AuthResourceController(JwtAuthenticationProvider provider,
                                  UserService userService) {
        this.authenticationProvider = provider;
        this.userService = userService;
    }
    @ApiOperation(value = "Получение jwt токена")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Jwt токен получен"),
            @ApiResponse(code = 400,message = "Invalid username or password")
    })
    @PostMapping("/token")
    public ResponseEntity<String> token(@RequestBody AuthRequest request) {
        String email = Objects.requireNonNull(request.getEmail());
        String password = Objects.requireNonNull(request.getPassword());
        try {
            SecurityContextHolder.getContext().setAuthentication(authenticationProvider
                    .authenticate(new UsernamePasswordAuthenticationToken(email, password)));
        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        String jwt = JWT.create().withJWTId(email).withSubject(password)
                .sign(Algorithm.HMAC256("PrinceNanadaime".getBytes()));

        return ResponseEntity.ok().body(jwt);
    }

    @PostMapping("/registration")
    @ApiOperation("Регистрация для пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "На вашу почту пришло сообщение о активации аккаунта"),
            @ApiResponse(code = 400, message = "Пользователь с таким email уже существует.")
    })
    public ResponseEntity<?> registration(@RequestBody RegistrationRequest rr) {
        try {
            this.userService.registration(rr);
        } catch (RuntimeException var3) {
            return ResponseEntity.badRequest().body(var3.getMessage());
        }

        return ResponseEntity.ok(String.format("You are registered by email : %s \n" +
                "An account activation message has arrived to your email", rr.getEmail()));
    }

    @GetMapping("/activate/{code}")
    @ApiOperation("Подтверждение почты пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "User active!"),
            @ApiResponse(code = 400, message = "Mistake! The user is already activated or the code itself is not correct.")
    })
    public ResponseEntity<?> activateCode(@PathVariable String code) {
        boolean isActivate = this.userService.activateUserByCode(code);
        return isActivate ? ResponseEntity.ok("User active!") : ResponseEntity.badRequest().body("Mistake! The user is already activated or the code itself is not correct.");
    }

}
