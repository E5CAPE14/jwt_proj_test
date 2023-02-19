package com.project.p_project.model.security.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class RegistrationRequest {

    private String email;
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birthday;
    private String password;
}
