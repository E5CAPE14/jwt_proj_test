package com.project.p_project.UserResourceControllerTest;

import com.project.p_project.PProjectApplicationTests;
import com.project.p_project.model.survey.dto.SurveyDtoRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserResourceControllerTest extends PProjectApplicationTests {

    //Сохранение результатов опроса текущего авторизированного пользователя
    @Test
    public void savingUserSurveyResultTest() throws Exception {
        String access_token_user = getJwtToken("user@mail.ru", "Password2");
        SurveyDtoRequest surveyDtoRequest = new SurveyDtoRequest(
                "Dish",
                "Song",
                LocalDate.of(1975,11,21),
                121L,
                "#ffafaf" //Pink
        );

        Assertions.assertEquals(0,em.createQuery("SELECT COUNT(s) from Survey s join s.user as u where u.email = 'user@mail.ru'",Long.class)
                .getSingleResult());

        mvc.perform(post("/api/user/survey")
                        .header("Authorization",access_token_user)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(surveyDtoRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        Assertions.assertEquals(1,em.createQuery("SELECT COUNT(s) from Survey s join s.user as u where u.email = 'user@mail.ru'",Long.class)
                .getSingleResult());
    }
    @Test
    public void savingUserSurveyResultIsExistTest() throws Exception {
        String access_token_admin = getJwtToken("admin@mail.ru", "12345");
        SurveyDtoRequest surveyDtoRequest = new SurveyDtoRequest(
                "Dish",
                "Song",
                LocalDate.of(1975,11,21),
                121L,
                "#ffafaf"
        );

        Assertions.assertEquals(1,em.createQuery("SELECT COUNT(s) from Survey s join s.user as u where u.email = 'admin@mail.ru'",Long.class)
                .getSingleResult());

        mvc.perform(post("/api/user/survey")
                        .header("Authorization",access_token_admin)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(surveyDtoRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    //Получение пройденного опроса авторизированного пользователя
    /*
            Тест может упасть, мы не используем FlyWay и after и before методы.
            Так как тесты выполняются не упорядоченно.
    */
    @Test
    public void getUserSurveyByCurrentUser() throws Exception {
        String access_token_user = getJwtToken("user@mail.ru", "Password2");
        String access_token_admin = getJwtToken("admin@mail.ru", "12345");

        mvc.perform(get("/api/user/survey")
                .header("Authorization",access_token_admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hexColor")
                        .value("#00ff00"))
                .andExpect(jsonPath("$.favoriteDish")
                        .value("FDish"))
                .andExpect(jsonPath("$.favoriteSong")
                        .value("FSong"))
                .andExpect(jsonPath("$.favoriteNumber")
                        .value(44))
                .andExpect(jsonPath("$.favoriteDate[0]")
                        .value(1999))
                .andExpect(jsonPath("$.favoriteDate[1]")
                        .value(6))
                .andExpect(jsonPath("$.favoriteDate[2]")
                        .value(14));

        mvc.perform(get("/api/user/survey")
                        .header("Authorization",access_token_user))
                .andExpect(status().isBadRequest());
    }

    //Получение авторизированного пользователя.
    @Test
    public void getCurrentUser() throws Exception {
        String access_token_user = getJwtToken("user@mail.ru", "Password2");
        String access_token_admin = getJwtToken("admin@mail.ru", "12345");

        mvc.perform(get("/api/user/")
                        .header("Authorization",access_token_admin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Алексей"))
                .andExpect(jsonPath("$.surname").value("Юный"))
                .andExpect(jsonPath("$.patronymic").value("Падаван"));
        mvc.perform(get("/api/user/")
                        .header("Authorization",access_token_user))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Дмитрий"))
                .andExpect(jsonPath("$.surname").value("Хан"))
                .andExpect(jsonPath("$.patronymic").value("Соло"));
    }
}
