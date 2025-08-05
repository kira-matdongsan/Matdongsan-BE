package com.example.matdongsan.domain.food.service.dto;

import com.example.matdongsan.jpa.entity.Food;
import com.example.matdongsan.jpa.entity.FoodStoryPlace;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class PlaceServiceDto {

    private final String name;
    private final String content;
    private final String category;
    private final String address;
    private final String naverUrl;
    private final List<String> imageUrls;

    public FoodStoryPlace toFoodStoryPlace(Food food) {
        return FoodStoryPlace.builder()
                .food(food)
                .userId(1L)
                .placeName(name)
                .category(category)
                .address(address)
                .naverUrl(naverUrl)
                .build();
    }

}
