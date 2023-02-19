package com.project.p_project.dao.impl;

import com.project.p_project.dao.abstracts.RoleDao;
import com.project.p_project.model.user.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.Optional;
@Repository
@Slf4j
public class RoleDaoImpl implements RoleDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Role> getRoleByName(String name) {
        log.info(String.format("Запрос роли методом getRoleByName с параметром %s",name));
        return entityManager.createQuery(
                        "SELECT r " +
                                "FROM Role r " +
                                "WHERE r.name = :name",
                        Role.class)
                .setParameter("name", name)
                .getResultStream()
                .findAny();
    }
    @Override
    @Transactional
    public void persist(Role role){
        log.info(String.format("Cохранение роли методом persist с параметром %s", role.toString()));
        entityManager.persist(role);
        entityManager.flush();
    }
}
