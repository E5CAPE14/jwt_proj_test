package com.project.p_project.model.survey.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class SurveyDtoResponse {

    private String FavoriteDish;
    private String FavoriteSong;
    private LocalDate FavoriteDate;
    private Long FavoriteNumber;
    private String hexColor;

    public SurveyDtoResponse(String favoriteDish,
                             String favoriteSong,
                             LocalDate favoriteDate,
                             Long favoriteNumber,
                             String hexColor
        ) {
        FavoriteDish = favoriteDish;
        FavoriteSong = favoriteSong;
        FavoriteDate = favoriteDate;
        FavoriteNumber = favoriteNumber;
        this.hexColor = hexColor;
    }
}
