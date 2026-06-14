package com.example.matdongsan.seasonaldiary.application.dto;

import com.example.matdongsan.food.enums.FoodStoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DiaryFoodStoryServiceDto {
    private Long storyId;
    private Long foodId;
    private String foodName;
    private FoodStoryType type;
    private String message;
}
