package com.example.matdongsan.jpa.repository;

import com.example.matdongsan.jpa.entity.dish.Dish;
import com.example.matdongsan.jpa.entity.food.FeaturedFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByFeaturedFoodOrderByVoteCountDesc(FeaturedFood featuredFood);
}
