package com.example.matdongsan.service;

import com.example.matdongsan.common.exception.ErrorCode;
import com.example.matdongsan.common.exception.CustomException;
import com.example.matdongsan.controller.dto.DishPickResponseDto;
import com.example.matdongsan.controller.dto.FoodInfoResponseDto;
import com.example.matdongsan.controller.dto.StoryImageResponseDto;
import com.example.matdongsan.controller.dto.StoryResponseDto;
import com.example.matdongsan.domain.Food;
import com.example.matdongsan.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodStoryRepository foodStoryRepository;
    private final FoodStoryImageRepository foodStoryImageRepository;

    public FoodInfoResponseDto getFoodInfoById(Long id) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
        return FoodInfoResponseDto.of(food, true);
    }

    // TODO: 맛동산 Pick 제철요리 투표 관련 기능 논의중
    public DishPickResponseDto getAllDishesByFoodId(Long id) {
        return DishPickResponseDto.builder().build();
    }

    public Page<StoryResponseDto> getAllStoriesByFoodId(Long id, Pageable pageable) {
        Food food = foodRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
        return foodStoryRepository.findAllByFoodAndDeletedAtIsNull(food, pageable)
                .map(foodStory -> {
                    List<StoryImageResponseDto> images = foodStoryImageRepository.findAllByFoodStoryAndDeletedAtIsNull(foodStory)
                            .stream()
                            .map(StoryImageResponseDto::of)
                            .toList();
                    return StoryResponseDto.of(foodStory, images, true);
                });
    }

    // TODO: [User] 계정 작업 후 구현 가능
    @Transactional
    public void likeFood(Long id) {

    }

}
