package com.example.matdongsan.service.dto;

import com.example.matdongsan.domain.Food;
import com.example.matdongsan.domain.FoodStorySeasonalNote;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class SeasonalNoteServiceDto {

    private final String content;
    private final LocalDate recordedDate;
    private final List<String> imageUrls;

    public FoodStorySeasonalNote toFoodStorySeasonalNote(Food food) {
        return FoodStorySeasonalNote.builder()
                .food(food)
                .userId(1L)
                .content(content)
                .recordedDate(recordedDate)
                .build();
    }

}
