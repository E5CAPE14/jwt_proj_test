package com.project.p_project.model.survey.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.p_project.model.user.User;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Accessors(chain = true)
public class SurveyDtoAdminResponse extends SurveyDtoResponse{

    public SurveyDtoAdminResponse(String favoriteDish, String favoriteSong, LocalDate favoriteDate, Long favoriteNumber, String hexColor) {
        super(favoriteDish, favoriteSong, favoriteDate, favoriteNumber, hexColor);
    }
    @JsonView
    private User user;

    public SurveyDtoAdminResponse(String favoriteDish,
                                  String favoriteSong,
                                  LocalDate favoriteDate,
                                  Long favoriteNumber,
                                  User user,
                                  String hexColor
    ) {
        super(favoriteDish, favoriteSong, favoriteDate, favoriteNumber, hexColor);
        this.user = user;
    }
}
