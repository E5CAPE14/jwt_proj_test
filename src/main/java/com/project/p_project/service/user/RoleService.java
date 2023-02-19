package com.project.p_project.service.user;

import com.project.p_project.model.user.Role;

import java.util.Optional;

public interface RoleService {
    Optional<Role> getRoleByName(String user);

    void persist(Role role);
}
