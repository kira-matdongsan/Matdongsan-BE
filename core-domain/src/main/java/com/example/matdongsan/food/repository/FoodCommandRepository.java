package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FeaturedFood;
import com.example.matdongsan.food.domain.Food;

import java.util.List;

public interface FoodCommandRepository {

    Food save(Food food);

    List<Food> saveAll(List<Food> foods);

    FeaturedFood saveFeaturedFood(FeaturedFood featuredFood);
}
