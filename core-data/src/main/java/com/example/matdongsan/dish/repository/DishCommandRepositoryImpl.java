package com.example.matdongsan.dish.repository;

import com.example.matdongsan.dish.domain.Dish;
import com.example.matdongsan.dish.mapper.DishMapper;
import com.example.matdongsan.jpa.entity.dish.DishEntity;
import com.example.matdongsan.jpa.repository.DishJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DishCommandRepositoryImpl implements DishCommandRepository {

    private final DishJpaRepository dishJpaRepository;
    private final DishMapper dishMapper;

    @Override
    public Dish save(Dish dish) {
        DishEntity entity = dishMapper.toEntity(dish);
        DishEntity saved = dishJpaRepository.save(entity);
        return dishMapper.toDomain(saved);
    }
}
