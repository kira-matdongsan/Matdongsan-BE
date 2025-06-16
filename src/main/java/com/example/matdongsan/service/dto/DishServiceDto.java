package com.example.matdongsan.service.dto;

import com.example.matdongsan.domain.Dish;
import com.example.matdongsan.domain.FeaturedFood;
import com.example.matdongsan.domain.Food;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class DishServiceDto {

    private final String name;
    private final List<String> imageUrls;

    public Dish toDish(Food food, FeaturedFood featuredFood) {
        return Dish.builder()
                .food(food)
                .featuredFood(featuredFood)
                .name(name)
                .voteCount(1)
                .build();
    }
}
