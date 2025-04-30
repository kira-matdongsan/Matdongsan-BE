package com.example.matdongsan.external.opendata.service;

import com.example.matdongsan.domain.Food;
import com.example.matdongsan.external.opendata.converter.ExternalFoodSourceToFoodConverter;
import com.example.matdongsan.repository.ExternalFoodSourceRepository;
import com.example.matdongsan.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FoodGenerateService {

    private final ExternalFoodSourceRepository externalRepo;
    private final FoodRepository foodRepo;
    private final ExternalFoodSourceToFoodConverter converter;

    public void generateAll() {
        List<Food> newFoods = externalRepo.findAll().stream()
                .filter(src -> !foodRepo.existsByName(src.getName()))
                .map(converter::convert)
                .toList();

        foodRepo.saveAll(newFoods);
    }
}
