package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.Dish;
import com.example.matdongsan.jpa.entity.DishVoteImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishVoteImageRepository extends JpaRepository<DishVoteImage, Long> {

    List<DishVoteImage> findAllByDishAndDeletedAtIsNullOrderByCreatedAtDesc(Dish dish);
}
