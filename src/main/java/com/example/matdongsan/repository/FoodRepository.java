package com.example.matdongsan.repository;

import com.example.matdongsan.domain.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository  extends JpaRepository<Food, Long> {
    boolean existsByName(String name);
}
