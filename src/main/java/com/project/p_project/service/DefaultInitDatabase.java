package com.project.p_project.service;

import com.project.p_project.dao.abstracts.SurveyDao;
import com.project.p_project.model.survey.Survey;
import com.project.p_project.model.user.Role;
import com.project.p_project.model.user.User;
import com.project.p_project.service.user.RoleService;
import com.project.p_project.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;

@Service
public class DefaultInitDatabase {

    private final UserService userService;
    private final RoleService roleService;

    private final SurveyDao surveyService;
    @Autowired
    public DefaultInitDatabase(UserService userService, RoleService roleService, SurveyDao surveyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.surveyService = surveyService;
        try {
            initData();
        } catch (Exception e){
            System.err.println("Изначальные данные уже есть!");
        }
    }
    /*
        Инициализация изначальной базы данных.
     */
    private void initData(){
        Role userR = new Role("ROLE_USER");
        Role adminR = new Role("ROLE_ADMIN");
        roleService.persist(userR);
        roleService.persist(adminR);
        User admin = new User("admin@mail.ru","Алексей","Юный","Падаван"
                ,LocalDate.of(1999,6,14),"12345",adminR);
        User user = new User("user@mail.ru","Дмитрий","Хан","Соло"
                ,LocalDate.of(1993,6,14),"Password2",userR);
        userService.persist(admin);
        userService.persist(user);
        surveyService.save(Survey.builder()
                        .favoriteSong("FSong")
                        .favoriteDish("FDish")
                        .favoriteDate(LocalDate.of(1999,6,14))
                        .favoriteNumber(44L)
                        .hexColor(toHex(Color.GREEN))
                        .user(admin)
                .build());
    }

    private String toHex(Color color){
        return String.format("#%02x%02x%02x",color.getRed(),color.getGreen(),color.getBlue());
    }

}
