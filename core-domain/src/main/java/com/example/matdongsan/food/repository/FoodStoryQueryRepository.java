package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;
import com.example.matdongsan.food.enums.FoodStoryType;

import java.util.List;
import java.util.Optional;

public interface FoodStoryQueryRepository {

    List<FoodStory> findAllByFoodId(Long foodId, FoodStoryType type, int page, int size, List<Long> blockedUserIds);

    long countByFoodId(Long foodId, FoodStoryType type, List<Long> blockedUserIds);

    long countLikesById(Long storyId);

    long countReportsById(Long storyId);

    boolean existsReportByStoryIdAndUserId(Long storyId, Long userId);

    Optional<FoodStory> findById(Long storyId);

    List<FoodStoryImage> findAllImagesById(Long storyId);
}
