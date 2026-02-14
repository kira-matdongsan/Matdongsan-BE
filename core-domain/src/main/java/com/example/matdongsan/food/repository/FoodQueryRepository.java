package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.food.domain.FeaturedFood;
import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;

import java.util.List;
import java.util.Optional;

public interface FoodQueryRepository {

    Optional<Food> findById(Long id);

    boolean existsByName(String name);

    Optional<FeaturedFood> findLatestFeaturedFoodByFoodId(Long foodId);

    List<FoodStory> findAllStoriesByFoodId(Long foodId, int page, int size);

    long countStoriesByFoodId(Long foodId);

    List<FoodStoryImage> findAllImagesByStoryId(Long storyId);

    Optional<Food> findCurrentFeaturedFood();
}
