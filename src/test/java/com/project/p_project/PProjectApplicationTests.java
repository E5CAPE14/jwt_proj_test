package com.project.p_project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.p_project.model.security.dto.AuthRequest;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.junit5.api.DBRider;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DBRider
@SpringBootTest(classes = PProjectApplication.class)
@TestPropertySource(properties = {"spring.config.location = src/test/resources/application-test.properties"})
@DBUnit(caseSensitiveTableNames = true, cacheConnection = false, allowEmptyFields = true)
@AutoConfigureMockMvc
@MockitoSettings
public class PProjectApplicationTests {
    @Autowired
    public MockMvc mvc;

    @PersistenceContext
    protected EntityManager em;

    @Autowired
    protected ObjectMapper objectMapper;

    public String getJwtToken(String email, String password) throws Exception {
        AuthRequest request = new AuthRequest();
        request.setEmail(email);
        request.setPassword(password);
        String json = objectMapper.writeValueAsString(request);
        MvcResult m = this.mvc.perform(post("/api/auth/token").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn();
        return "Bearer " + m.getResponse().getContentAsString();
    }
}
