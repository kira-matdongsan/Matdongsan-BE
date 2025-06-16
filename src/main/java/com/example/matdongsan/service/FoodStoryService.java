package com.example.matdongsan.service;

import com.example.matdongsan.common.exception.ErrorCode;
import com.example.matdongsan.common.exception.CustomException;
import com.example.matdongsan.controller.dto.StoryImageResponseDto;
import com.example.matdongsan.controller.dto.StoryResponseDto;
import com.example.matdongsan.domain.*;
import com.example.matdongsan.repository.*;
import com.example.matdongsan.service.dto.PlaceServiceDto;
import com.example.matdongsan.service.dto.RecipeServiceDto;
import com.example.matdongsan.service.dto.SeasonalNoteServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FoodStoryService {

    private final FoodRepository foodRepository;
    private final FoodStoryRepository foodStoryRepository;
    private final FoodStorySeasonalNoteRepository foodStorySeasonalNoteRepository;
    private final FoodStoryRecipeRepository foodStoryRecipeRepository;
    private final FoodStoryPlaceRepository foodStoryPlaceRepository;
    private final FoodStoryImageRepository foodStoryImageRepository;

    @Transactional
    public StoryResponseDto createSeasonalNoteStory(Long foodId, SeasonalNoteServiceDto serviceDto) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FoodStorySeasonalNote story = foodStorySeasonalNoteRepository.save(serviceDto.toFoodStorySeasonalNote(food));

        List<String> imageUrls = serviceDto.getImageUrls();
        List<FoodStoryImage> images = IntStream.range(0, imageUrls.size())
                .mapToObj(i -> FoodStoryImage.builder()
                        .foodStory(story)
                        .imageUrl(imageUrls.get(i))
                        .orderNum(i)
                        .build())
                .collect(Collectors.toList());

        foodStoryImageRepository.saveAll(images);

        return StoryResponseDto.ofSeasonalNoteStory(story, images.stream().map(StoryImageResponseDto::of).toList());
    }

    @Transactional
    public StoryResponseDto createRecipeStory(Long foodId, RecipeServiceDto serviceDto) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FoodStoryRecipe story = foodStoryRecipeRepository.save(serviceDto.toFoodStoryRecipe(food));

        List<String> imageUrls = serviceDto.getImageUrls();
        List<FoodStoryImage> images = IntStream.range(0, imageUrls.size())
                .mapToObj(i -> FoodStoryImage.builder()
                        .foodStory(story)
                        .imageUrl(imageUrls.get(i))
                        .orderNum(i)
                        .build())
                .collect(Collectors.toList());

        foodStoryImageRepository.saveAll(images);

        return StoryResponseDto.ofRecipeStory(story, images.stream().map(StoryImageResponseDto::of).toList());
    }

    @Transactional
    public StoryResponseDto createPlaceStory(Long foodId, PlaceServiceDto serviceDto) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FoodStoryPlace story = foodStoryPlaceRepository.save(serviceDto.toFoodStoryPlace(food));

        List<String> imageUrls = serviceDto.getImageUrls();
        List<FoodStoryImage> images = IntStream.range(0, imageUrls.size())
                .mapToObj(i -> FoodStoryImage.builder()
                        .foodStory(story)
                        .imageUrl(imageUrls.get(i))
                        .orderNum(i)
                        .build())
                .collect(Collectors.toList());

        foodStoryImageRepository.saveAll(images);

        return StoryResponseDto.ofPlaceStory(story, images.stream().map(StoryImageResponseDto::of).toList());
    }
}
