package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.food.FoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodJpaRepository extends JpaRepository<FoodEntity, Long> {
    boolean existsByName(String name);
}
