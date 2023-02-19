package com.project.p_project.dao.impl;

import com.project.p_project.dao.abstracts.SurveyDao;
import com.project.p_project.dao.util.SingleResultUtil;
import com.project.p_project.model.survey.Survey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class SurveyDaoImpl implements SurveyDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Survey> getAll() {
        log.info("Запрос всех опросов методом getAll");
        return entityManager.createQuery("SELECT s FROM Survey s join fetch s.user",Survey.class)
                .getResultList();
    }

    @Override
    public Optional<Survey> getSurveyById(Long id) {
        log.info(String.format("Запрос опроса методом getSurveyById с параметром %d",id));
        TypedQuery<Survey> surveyTypedQuery =
                entityManager.createQuery("SELECT s FROM Survey s join fetch s.user where s.id = :id",Survey.class)
                .setParameter("id",id);
        return SingleResultUtil.getSingleResultOrNull(surveyTypedQuery);
    }

    @Override
    public Optional<Survey> getSurveyByUserEmail(String email) {
        log.info(String.format("Запрос опроса методом getSurveyByUserEmail с параметром %s",email));
        TypedQuery<Survey> surveyTypedQuery =
                entityManager.createQuery("SELECT s FROM Survey s join fetch s.user where s.user.email = :email",Survey.class)
                        .setParameter("email",email);
        return SingleResultUtil.getSingleResultOrNull(surveyTypedQuery);
    }

    @Override
    @Transactional
    public void save(Survey survey) {
        log.info(String.format("Сохранение опроса методом save с параметром %s",survey.toString()));
        entityManager.persist(survey);
    }
}
