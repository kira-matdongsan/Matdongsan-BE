package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;

import java.util.List;

public interface FoodStoryCommandRepository {

    FoodStory save(FoodStory story);

    List<FoodStoryImage> saveAllImages(List<FoodStoryImage> images);

    void deleteById(Long storyId);
}
