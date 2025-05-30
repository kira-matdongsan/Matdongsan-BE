package com.example.matdongsan.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RecipeServiceDto {

    private final String name;
    private final String ingredients;
    private final String instructions;

}
