package com.example.matdongsan.repository;

import com.example.matdongsan.domain.FeaturedFood;
import com.example.matdongsan.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeaturedFoodRepository extends JpaRepository<FeaturedFood, Long> {

    Optional<FeaturedFood> findFirstByFoodOrderByStartAtDesc(Food food);
}
