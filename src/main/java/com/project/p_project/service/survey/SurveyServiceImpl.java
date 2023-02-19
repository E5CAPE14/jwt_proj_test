package com.project.p_project.service.survey;

import com.project.p_project.dao.abstracts.SurveyDao;
import com.project.p_project.dao.abstracts.UserDao;
import com.project.p_project.model.survey.Survey;
import com.project.p_project.model.survey.dto.SurveyDtoAdminResponse;
import com.project.p_project.model.survey.dto.SurveyDtoRequest;
import com.project.p_project.model.survey.dto.SurveyDtoResponse;
import com.project.p_project.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class SurveyServiceImpl implements SurveyService{

    private final SurveyDao surveyDao;
    private final UserDao userDao;

    @Autowired
    public SurveyServiceImpl(SurveyDao surveyDao, UserDao userDao) {
        this.surveyDao = surveyDao;
        this.userDao = userDao;
    }

    @Override
    public List<SurveyDtoAdminResponse> getAll() {
        List<Survey> surveys = surveyDao.getAll();
        List<SurveyDtoAdminResponse> surveyDtoResponses = new ArrayList<>();
        for (var el:surveys) {
            Color color = Color.decode(el.getHexColor());
            surveyDtoResponses.add(
                    new SurveyDtoAdminResponse(el.getFavoriteDish(),
                            el.getFavoriteSong(),
                            el.getFavoriteDate(),
                            el.getFavoriteNumber(),
                            el.getUser(),
                            el.getHexColor())
            );
        }
        return surveyDtoResponses;
    }

    @Override
    public SurveyDtoAdminResponse getSurveyById(Long id) {

        Survey survey = surveyDao.getSurveyById(id).orElseThrow(() -> new RuntimeException("Такого опроса не существует"));
        Color color = Color.decode(survey.getHexColor());

        return new SurveyDtoAdminResponse(survey.getFavoriteDish(),
                survey.getFavoriteSong(),
                survey.getFavoriteDate(),
                survey.getFavoriteNumber(),
                survey.getUser(),
                survey.getHexColor());
    }

    @Override
    public SurveyDtoResponse getSurveyByCurrentUser() {
        User user = userDao.getByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).get();

        Survey survey = surveyDao.getSurveyByUserEmail(user.getEmail()).orElseThrow(() ->
                new RuntimeException("Пользователь c данным email не проходил опрос!"));
        Color color = Color.decode(survey.getHexColor());

        return new SurveyDtoResponse(survey.getFavoriteDish(),
                survey.getFavoriteSong(),
                survey.getFavoriteDate(),
                survey.getFavoriteNumber(),
                survey.getHexColor());
    }

    @Override
    public SurveyDtoAdminResponse getSurveyByUserEmail(String email) {
        Survey survey = surveyDao.getSurveyByUserEmail(email).orElseThrow(() ->
                new RuntimeException("Пользователь c данным email не проходил опрос!"));
        Color color = Color.decode(survey.getHexColor());

        return new SurveyDtoAdminResponse(survey.getFavoriteDish(),
                survey.getFavoriteSong(),
                survey.getFavoriteDate(),
                survey.getFavoriteNumber(),
                survey.getUser(),
                survey.getHexColor());
    }

    @Override
    public void save(SurveyDtoRequest surveyDTO) {
        User user = userDao.getByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString()).get();

        if(surveyDao.getSurveyByUserEmail(user.getEmail()).isEmpty()) {
            Survey survey = Survey.builder()
                    .favoriteDate(surveyDTO.getFavoriteDate())
                    .favoriteDish(surveyDTO.getFavoriteDish())
                    .favoriteNumber(surveyDTO.getFavoriteNumber())
                    .favoriteSong(surveyDTO.getFavoriteSong())
                    .user(user)
                    .hexColor(surveyDTO.getHexColor())
                    .build();

            surveyDao.save(survey);
            return;
        }

        throw new RuntimeException("Пользователь уже проголосовал");
    }
}
