package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.dish.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishJpaRepository extends JpaRepository<DishEntity, Long> {
}
