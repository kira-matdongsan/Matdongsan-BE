package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.food.FoodStoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodStoryJpaRepository extends JpaRepository<FoodStoryEntity, Long> {
}
