package com.project.p_project.service.user;

import com.project.p_project.model.security.dto.RegistrationRequest;
import com.project.p_project.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getByEmail(String email);
    List<User> getAll();
    void deleteByEmail(String email);
    void updatePasswordByEmail(String email, String password);
    Boolean isPresentByEmail(String email);
    void persist(User user);
    void update(User user);
    boolean activateUserByCode(String code);
    void registration(RegistrationRequest rr);
}
