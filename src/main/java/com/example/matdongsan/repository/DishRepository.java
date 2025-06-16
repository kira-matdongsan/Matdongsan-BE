package com.example.matdongsan.repository;

import com.example.matdongsan.domain.Dish;
import com.example.matdongsan.domain.FeaturedFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findAllByFeaturedFoodOrderByVoteCountDesc(FeaturedFood featuredFood);
}
