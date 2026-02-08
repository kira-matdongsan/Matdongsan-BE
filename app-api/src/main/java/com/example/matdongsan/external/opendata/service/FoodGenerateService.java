package com.example.matdongsan.external.opendata.service;

import com.example.matdongsan.food.domain.Food;
import com.example.matdongsan.external.opendata.converter.ExternalFoodSourceToFoodConverter;
import com.example.matdongsan.food.repository.FoodCommandRepository;
import com.example.matdongsan.food.repository.FoodQueryRepository;
import com.example.matdongsan.jpa.repository.ExternalFoodSourceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodGenerateService {

    private final ExternalFoodSourceJpaRepository externalRepo;
    private final FoodQueryRepository foodQueryRepository;
    private final FoodCommandRepository foodCommandRepository;
    private final ExternalFoodSourceToFoodConverter converter;

    public void generateAll() {
        List<Food> newFoods = externalRepo.findAll().stream()
                .filter(src -> !foodQueryRepository.existsByName(src.getName()))
                .map(converter::convert)
                .toList();

        foodCommandRepository.saveAll(newFoods);
    }
}
