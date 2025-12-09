package com.example.matdongsan.food.application.service;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.food.application.dto.PlaceServiceDto;
import com.example.matdongsan.food.application.dto.RecipeServiceDto;
import com.example.matdongsan.food.application.dto.SeasonalNoteServiceDto;
import com.example.matdongsan.food.presentation.response.StoryImageResponse;
import com.example.matdongsan.food.presentation.response.StoryResponse;
import com.example.matdongsan.jpa.entity.food.*;
import com.example.matdongsan.jpa.repository.*;
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
    public StoryResponse createSeasonalNoteStory(Long foodId, SeasonalNoteServiceDto serviceDto) {
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

        return StoryResponse.ofSeasonalNoteStory(story, images.stream().map(StoryImageResponse::of).toList());
    }

    @Transactional
    public StoryResponse createRecipeStory(Long foodId, RecipeServiceDto serviceDto) {
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

        return StoryResponse.ofRecipeStory(story, images.stream().map(StoryImageResponse::of).toList());
    }

    @Transactional
    public StoryResponse createPlaceStory(Long foodId, PlaceServiceDto serviceDto) {
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

        return StoryResponse.ofPlaceStory(story, images.stream().map(StoryImageResponse::of).toList());
    }
}
