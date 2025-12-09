package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.food.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByName(String name);
}
