package com.project.p_project.dao.impl;

import com.project.p_project.dao.abstracts.UserDao;
import com.project.p_project.dao.util.SingleResultUtil;
import com.project.p_project.model.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> getByEmail(String email) {
        log.info(String.format("Запрос пользователя из БД в методе getByEmail c параметром %s",email));
        TypedQuery<User> query = entityManager.createQuery(
                        "SELECT u " +
                                "FROM User u " +
                                "INNER JOIN FETCH u.role " +
                                "WHERE u.email = :email AND u.active = 'isActive' ",
                        User.class)
                .setParameter("email", email);
        return SingleResultUtil.getSingleResultOrNull(query);
    }
    @Override
    public Boolean isPresentByEmail(String email) {
        log.info(String.format("Проверка наличия пользователя в БД в методе isPresentByEmail с параметром %s",email));
        return entityManager.createQuery(
                        "SELECT COUNT(u)>0 " +
                                "FROM User u " +
                                "WHERE u.email = :email ",
                        Long.class)
                .setParameter("email", email)
                .getSingleResult() == 1;
    }
    @Override
    public List<User> getAll() {
        log.info("Запрос всех пользователей методом getAll");
        return entityManager.createQuery("SELECT u " +
                        "FROM User u " +
                        "where u.active = 'isActive'", User.class)
                .getResultList();
    }
    @Override
    public void deleteByEmail(String email) {
        log.info(String.format("Запрос на удаление пользователя из БД методом deleteByEmail с параметром %s",email));
        entityManager.createQuery(
                        "DELETE FROM User u where u.email = :email ")
                .setParameter("email", email)
                .executeUpdate();
    }
    @Override
    public void updatePasswordByEmail (String email, String password) {
        log.info(String.format("Запрос на обновление пароля пользователя из БД" +
                " методом updatePasswordByEmail с параметрами email : %s , encodePassword : %s",email,password));
        entityManager.createQuery(
                        "UPDATE User u " +
                                "SET u.password = :password " +
                                "WHERE u.email = :email")
                .setParameter("password", password)
                .setParameter("email", email).executeUpdate();
    }
    @Transactional
    @Override
    public void persist(User user) {
        log.info(String.format("Сохранение пользователя методом persist с параметром %s",user.toString()));
        try {
            entityManager.persist(user);
        } catch (ConstraintViolationException e){
            throw new RuntimeException("Что-то пошло не так.\n Проверьте поле Email на соответствие и остальные поля на null");
        }
        entityManager.flush();
    }
    @Transactional
    @Override
    public void update(User user) {
        log.info("Обновление пользователя методом update");
        entityManager.merge(user);
    }

    @Override
    public Optional<User> findUserByActivateCode(String code) {
        log.info(String.format("Поиск пользователя по коду активации : %s",code));
        TypedQuery<User> user = entityManager.createQuery("SELECT u FROM User u where u.active = :code",User.class)
                .setParameter("code",code);

        return SingleResultUtil.getSingleResultOrNull(user);
    }
}
