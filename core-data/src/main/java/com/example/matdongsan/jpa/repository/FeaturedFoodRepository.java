package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.food.FeaturedFood;
import com.example.matdongsan.jpa.entity.food.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeaturedFoodRepository extends JpaRepository<FeaturedFood, Long> {

    Optional<FeaturedFood> findFirstByFoodOrderByStartAtDesc(Food food);
}
