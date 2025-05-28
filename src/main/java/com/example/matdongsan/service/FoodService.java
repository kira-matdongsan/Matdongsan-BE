package com.example.matdongsan.service;

import com.example.matdongsan.controller.dto.DishResponseDto;
import com.example.matdongsan.controller.dto.FoodInfoResponseDto;
import com.example.matdongsan.controller.dto.StoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FoodService {

    public FoodInfoResponseDto getFoodInfoById(Long id) {
        return FoodInfoResponseDto.builder().build();
    }

    public DishResponseDto getAllDishesByFoodId(Long id) {
        return DishResponseDto.builder().build();
    }

    public Page<StoryResponseDto> getAllStoriesByFoodId(Long id, Pageable pageable) {
        return new PageImpl<>(List.of(StoryResponseDto.builder().build()));
    }

    @Transactional
    public void likeFood(Long id) {

    }

}
