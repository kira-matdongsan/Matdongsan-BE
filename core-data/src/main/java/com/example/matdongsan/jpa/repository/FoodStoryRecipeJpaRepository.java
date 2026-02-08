package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.food.FoodStoryRecipeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodStoryRecipeJpaRepository extends JpaRepository<FoodStoryRecipeEntity, Long> {
}
