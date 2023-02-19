package com.project.p_project.controller;

import com.project.p_project.model.survey.dto.SurveyDtoAdminResponse;
import com.project.p_project.service.survey.SurveyService;
import com.project.p_project.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequestMapping("/api/admin/")
@RestController
@Api(value = "Работа с данными из под админа" , tags = "Admin")
public class AdminResourceController {

    private final UserService userService;
    private final SurveyService surveyService;

    @Autowired
    public AdminResourceController(UserService userService, SurveyService surveyService) {
        this.userService = userService;
        this.surveyService = surveyService;
    }

    @GetMapping("/getAllSurvey")
    @ApiOperation(value = "Получение всех заполненных форм для опроса")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Cписок всех заполненных опросов получен")
    })
    public List<SurveyDtoAdminResponse> getAllSurvey(){
        return surveyService.getAll();
    }

    @GetMapping("/getUsers")
    @ApiOperation(value = "Получение всех пользователей и администраторов")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Список всех пользователей получен")
    })
    public ResponseEntity<?> getUserAll(){
        return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("/getSurveyByEmail/{email}")
    @ApiOperation(value = "Получение заполненной формы опроса по email")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Форма заполненного опроса по email получена"),
            @ApiResponse(code = 400,message = "Пользователь c данным email не проходил опрос!")
    })
    public ResponseEntity<?> getSurveyByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(surveyService.getSurveyByUserEmail(email));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getSurveyById/{id}")
    @ApiOperation(value = "Получение заполненной формы опроса по id")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Форма заполненного опроса по id получена"),
            @ApiResponse(code = 400,message = "Такого опроса не существует")
    })
    public ResponseEntity<?> getSurveyById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(surveyService.getSurveyById(id));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
