package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;

import java.util.List;

public interface FoodCommandRepository {

    Food save(Food food);

    List<Food> saveAll(List<Food> foods);

    FoodStory saveStory(FoodStory story);

    List<FoodStoryImage> saveAllImages(List<FoodStoryImage> images);

    void deleteStory(Long storyId);
}
