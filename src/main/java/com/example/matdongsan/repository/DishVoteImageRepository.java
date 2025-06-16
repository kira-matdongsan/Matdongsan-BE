package com.example.matdongsan.repository;

import com.example.matdongsan.domain.Dish;
import com.example.matdongsan.domain.DishVoteImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishVoteImageRepository extends JpaRepository<DishVoteImage, Long> {

    List<DishVoteImage> findAllByDishAndDeletedAtIsNullOrderByCreatedAtDesc(Dish dish);
}
