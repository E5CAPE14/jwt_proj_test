package com.project.p_project.service.user;

import com.project.p_project.dao.abstracts.RoleDao;
import com.project.p_project.model.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(@Autowired RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Optional<Role> getRoleByName(String user) {
        return roleDao.getRoleByName(user);
    }
    @Override
    public void persist(Role role){
        roleDao.persist(role);
    }
}
