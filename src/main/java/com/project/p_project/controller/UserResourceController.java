package com.project.p_project.controller;

import com.project.p_project.model.survey.dto.SurveyDtoRequest;
import com.project.p_project.service.survey.SurveyService;
import com.project.p_project.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
@Api(value = "Работа с приложением из под пользователя", tags = {"User"})
public class UserResourceController {

    private final SurveyService surveyService;

    private final UserService userService;

    @Autowired
    public UserResourceController(SurveyService surveyService, UserService userService) {
        this.surveyService = surveyService;
        this.userService = userService;
    }

    @PostMapping("/survey")
    @ApiOperation(value = "Сохранение результатов опроса текущего авторизированного пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Спасибо за прохождение данной анкеты"),
            @ApiResponse(code = 400,message = "Пользователь уже проголосовал"),
    })
    public ResponseEntity<String> saveSurvey(@RequestBody SurveyDtoRequest surveyDto){
        try {
            surveyService.save(surveyDto);
            return ResponseEntity.ok("Спасибо за прохождение данной анкеты");
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/survey")
    @ApiOperation(value = "Получение пройденного опроса авторизированного пользователя")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Спасибо за прохождение данной анкеты"),
            @ApiResponse(code = 400,message = "Пользователь уже проголосовал")
    })
    public ResponseEntity<?> getCurrentSurvey(){
        try {
            return ResponseEntity.ok(surveyService.getSurveyByCurrentUser());
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @ApiOperation(value = "Получение авторизированного пользователя.")
    @ApiResponse(code = 200,message = "Пользователь получен")
    public ResponseEntity<?> getCurrentUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getByEmail(email));
    }

}

