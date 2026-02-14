package com.example.matdongsan.food.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DishServiceDto {

    private final Long id;
    private final String name;
    private final String thumbnailUrl;
    private final Integer rank;
    private final Integer voteCount;
}
