package com.project.p_project.dao.abstracts;

import com.project.p_project.model.user.Role;

import java.util.Optional;

public interface RoleDao {

    Optional<Role> getRoleByName(String name);
    void persist(Role role);

}
