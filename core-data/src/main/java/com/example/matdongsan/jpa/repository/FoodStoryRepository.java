package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.food.Food;
import com.example.matdongsan.jpa.entity.food.FoodStory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodStoryRepository extends JpaRepository<FoodStory, Long> {

    Page<FoodStory> findAllByFoodAndDeletedAtIsNull(Food food, Pageable pageable);
}
