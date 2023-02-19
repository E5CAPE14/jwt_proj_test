package com.project.p_project.service.survey;

import com.project.p_project.model.survey.dto.SurveyDtoAdminResponse;
import com.project.p_project.model.survey.dto.SurveyDtoRequest;
import com.project.p_project.model.survey.dto.SurveyDtoResponse;

import java.util.List;

public interface SurveyService {

    List<SurveyDtoAdminResponse> getAll();
    SurveyDtoResponse getSurveyById(Long id);

    SurveyDtoResponse getSurveyByCurrentUser();

    SurveyDtoResponse getSurveyByUserEmail(String email);

    void save(SurveyDtoRequest surveyDTO);
}
