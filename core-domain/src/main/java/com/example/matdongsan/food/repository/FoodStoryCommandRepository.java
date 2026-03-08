package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;
import com.example.matdongsan.food.domain.FoodStoryReport;
import com.example.matdongsan.food.enums.FoodStoryVisibility;

import java.util.List;

public interface FoodStoryCommandRepository {

    FoodStory save(FoodStory story);

    List<FoodStoryImage> saveAllImages(List<FoodStoryImage> images);

    FoodStoryReport saveReport(FoodStoryReport report);

    void updateVisibility(Long storyId, FoodStoryVisibility visibility);

    void deleteById(Long storyId);
}
