package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.DishVoteImage;

import java.util.List;

public interface DishVoteQueryRepository {

    long countVotesByDishId(Long dishId);

    long countTotalVotesByFeaturedFoodId(Long featuredFoodId);

    List<DishVoteImage> findAllActiveImagesByDishId(Long dishId);
}
