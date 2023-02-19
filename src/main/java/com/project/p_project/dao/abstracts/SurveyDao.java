package com.project.p_project.dao.abstracts;

import com.project.p_project.model.survey.Survey;
import com.project.p_project.model.user.User;

import java.util.List;
import java.util.Optional;

public interface SurveyDao {

    List<Survey> getAll();
    Optional<Survey> getSurveyById(Long id);
    Optional<Survey> getSurveyByUserEmail(String email);


    void save(Survey survey);
}
