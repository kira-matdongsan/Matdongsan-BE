package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FeaturedFood;
import com.example.matdongsan.food.domain.Food;

import java.util.Optional;

public interface FoodQueryRepository {

    Optional<Food> findById(Long id);

    boolean existsByName(String name);

    Optional<FeaturedFood> findFeaturedFoodById(Long id);

    Optional<FeaturedFood> findLatestFeaturedFoodByFoodId(Long foodId);

    Optional<FeaturedFood> findActiveFeaturedFoodByFoodId(Long foodId);

    Optional<Food> findCurrentFeaturedFood();
}
