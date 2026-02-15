package com.example.matdongsan.food.application.dto;

import com.example.matdongsan.food.enums.FoodStoryType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StoryParam {

    private final int page;
    private final int size;
    private final FoodStoryType type;
}
