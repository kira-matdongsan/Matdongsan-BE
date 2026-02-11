package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.food.FeaturedFoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeaturedFoodJpaRepository extends JpaRepository<FeaturedFoodEntity, Long> {

    Optional<FeaturedFoodEntity> findFirstByFoodIdOrderByStartAtDesc(Long foodId);
}
