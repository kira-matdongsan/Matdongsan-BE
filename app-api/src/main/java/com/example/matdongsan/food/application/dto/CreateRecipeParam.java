package com.example.matdongsan.food.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreateRecipeParam {

    private final String name;
    private final String ingredients;
    private final String instructions;
    private final List<String> imageUrls;
}
