package com.project.p_project.AdminControllerTest;

import com.mysql.cj.xdevapi.JsonString;
import com.project.p_project.PProjectApplicationTests;
import com.project.p_project.model.survey.dto.SurveyDtoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import springfox.documentation.spring.web.json.Json;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminResourceControllerTest extends PProjectApplicationTests {

    // Получение всех пользователей
    @Test
    public void allUsersFromUnderTheAdmin() throws Exception {
        String access_token_admin = getJwtToken("admin@mail.ru", "12345");
        mvc.perform(get("/api/admin/getUsers")
                        .header("Authorization", access_token_admin))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()")
                        .value(2));
        /*
            Можно добавить проверки на данные вьюшки
         */
    }
    @Test
    public void allUsersFromUnderTheUser() throws Exception {
        String access_token = getJwtToken("user@mail.ru", "Password2");
        mvc.perform(get("/api/admin/getUsers")
                        .header("Authorization", access_token))
                .andExpect(status().isForbidden());
    }

    // Получение заполненной формы опроса по email
    @Test
    public void getSurveyUserByEmailUnderTheUser() throws Exception {
        String access_token = getJwtToken("user@mail.ru", "Password2");
        mvc.perform(get("/api/user/survey")
                        .header("Authorization", access_token))
                .andExpect(status().is4xxClientError())
                .andExpect(status().isBadRequest());

    }
    @Test
    public void getSurveyUserByEmailUnderTheAdmin() throws Exception {
        String access_token = getJwtToken("admin@mail.ru", "12345");
        mvc.perform(get("/api/admin/getSurveyByEmail/admin@mail.ru")
                        .header("Authorization", access_token))
                .andExpect(status().isOk());
        /*
            Можно добавить проверки на данные вьюшки
         */
    }

    //Получение заполненной формы опроса по id опроса
    @Test
    public void getSurveyUserByIDUnderTheUser() throws Exception {
        String access_token = getJwtToken("user@mail.ru", "Password2");
        mvc.perform(get("/api/admin/getSurveyById/1")
                        .header("Authorization", access_token))
                .andExpect(status().isForbidden());

    }
    @Test
    public void getSurveyUserByIDUnderTheAdmin() throws Exception {
        String access_token = getJwtToken("admin@mail.ru", "12345");
        mvc.perform(get("/api/admin/getSurveyById/1")
                        .header("Authorization", access_token))
                .andExpect(status().isOk());
        /*
            Можно добавить проверки на данные вьюшки
         */
    }

    //Все опросы
    @Test
    public void getAllSurveyUnderTheUser() throws Exception {
        String access_token = getJwtToken("user@mail.ru", "Password2");
        mvc.perform(get("/api/admin/getAllSurvey")
                        .header("Authorization", access_token))
                .andExpect(status().isForbidden());
    }
    @Test
    public void getAllSurveyUnderTheAdmin() throws Exception {
        String access_token = getJwtToken("admin@mail.ru", "12345");
        mvc.perform(get("/api/admin/getAllSurvey")
                        .header("Authorization", access_token))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()")
                        .value(1));
        /*
            Можно добавить проверки на данные вьюшки
         */
    }

}
