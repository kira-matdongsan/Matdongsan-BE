package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.Dish;

public interface DishCommandRepository {

    Dish save(Dish dish);
}
