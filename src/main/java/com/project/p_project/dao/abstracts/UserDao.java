package com.project.p_project.dao.abstracts;

import com.project.p_project.model.user.User;

import java.util.List;
import java.util.Optional;
public interface UserDao {
    Optional<User> getByEmail(String email);
    List<User> getAll();
    void deleteByEmail(String email);
    void updatePasswordByEmail(String email, String password);
    Boolean isPresentByEmail(String email);
    void persist(User user);
    void update(User user);
    Optional<User> findUserByActivateCode(String code);
}
