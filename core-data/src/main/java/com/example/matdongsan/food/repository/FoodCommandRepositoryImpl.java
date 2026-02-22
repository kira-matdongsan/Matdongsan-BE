package com.example.matdongsan.food.repository;

import com.example.matdongsan.food.domain.FeaturedFood;
import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.food.mapper.FoodMapper;
import com.example.matdongsan.jpa.entity.food.FeaturedFoodEntity;
import com.example.matdongsan.jpa.entity.food.FoodEntity;
import com.example.matdongsan.jpa.repository.FeaturedFoodJpaRepository;
import com.example.matdongsan.jpa.repository.FoodJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FoodCommandRepositoryImpl implements FoodCommandRepository {

    private final FoodJpaRepository foodJpaRepository;
    private final FeaturedFoodJpaRepository featuredFoodJpaRepository;
    private final FoodMapper foodMapper;

    @Override
    public Food save(Food food) {
        FoodEntity entity = foodMapper.toFoodEntity(food);
        FoodEntity saved = foodJpaRepository.save(entity);
        return foodMapper.toFoodDomain(saved);
    }

    @Override
    public List<Food> saveAll(List<Food> foods) {
        List<FoodEntity> entities = foods.stream()
                .map(foodMapper::toFoodEntity)
                .collect(Collectors.toList());
        List<FoodEntity> saved = foodJpaRepository.saveAll(entities);
        return saved.stream().map(foodMapper::toFoodDomain).toList();
    }

    @Override
    public FeaturedFood saveFeaturedFood(FeaturedFood featuredFood) {
        FeaturedFoodEntity entity = foodMapper.toFeaturedFoodEntity(featuredFood);
        FeaturedFoodEntity saved = featuredFoodJpaRepository.save(entity);
        return foodMapper.toFeaturedFoodDomain(saved);
    }
}
