package com.example.matdongsan.service.dto;

import com.example.matdongsan.domain.Food;
import com.example.matdongsan.domain.FoodStoryRecipe;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class RecipeServiceDto {

    private final String name;
    private final String ingredients;
    private final String instructions;
    private final List<String> imageUrls;

    public FoodStoryRecipe toFoodStoryRecipe(Food food) {
        return FoodStoryRecipe.builder()
                .food(food)
                .userId(1L)
                .recipeName(name)
                .ingredients(ingredients)
                .instructions(instructions)
                .build();
    }
}
