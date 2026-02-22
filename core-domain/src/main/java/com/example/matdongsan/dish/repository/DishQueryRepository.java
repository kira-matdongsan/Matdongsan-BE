package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.domain.DishVoteImage;

import java.util.List;
import java.util.Optional;

public interface DishQueryRepository {

    Optional<Dish> findById(Long id);

    List<Dish> findAllByFeaturedFoodIdOrderByVoteCountDesc(Long featuredFoodId);

    long countVotesByDishId(Long dishId);

    long countTotalVotesByFeaturedFoodId(Long featuredFoodId);

    List<DishVoteImage> findAllActiveImagesByDishId(Long dishId);
}
