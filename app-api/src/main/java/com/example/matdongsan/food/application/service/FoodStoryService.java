package com.example.matdongsan.food.application.service;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.food.application.dto.CreatePlaceParam;
import com.example.matdongsan.food.application.dto.CreateRecipeParam;
import com.example.matdongsan.food.application.dto.CreateSeasonalNoteParam;
import com.example.matdongsan.food.application.dto.FoodStoryServiceDto;
import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;
import com.example.matdongsan.food.repository.FoodCommandRepository;
import com.example.matdongsan.food.repository.FoodQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FoodStoryService {

    private final FoodQueryRepository foodQueryRepository;
    private final FoodCommandRepository foodCommandRepository;

    @Transactional
    public FoodStoryServiceDto createSeasonalNoteStory(Long foodId, CreateSeasonalNoteParam param) {
        foodQueryRepository.findById(foodId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FoodStory story = FoodStory.createSeasonalNote(foodId, 1L, param.getContent(), param.getRecordedDate());
        FoodStory savedStory = foodCommandRepository.saveStory(story);

        List<FoodStoryImage> images = createImages(savedStory.getId(), param.getImageUrls());
        List<FoodStoryImage> savedImages = foodCommandRepository.saveAllImages(images);

        return FoodStoryServiceDto.from(savedStory, savedImages);
    }

    @Transactional
    public FoodStoryServiceDto createRecipeStory(Long foodId, CreateRecipeParam param) {
        foodQueryRepository.findById(foodId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FoodStory story = FoodStory.createRecipe(foodId, 1L, param.getName(), param.getIngredients(), param.getInstructions());
        FoodStory savedStory = foodCommandRepository.saveStory(story);

        List<FoodStoryImage> images = createImages(savedStory.getId(), param.getImageUrls());
        List<FoodStoryImage> savedImages = foodCommandRepository.saveAllImages(images);

        return FoodStoryServiceDto.from(savedStory, savedImages);
    }

    @Transactional
    public FoodStoryServiceDto createPlaceStory(Long foodId, CreatePlaceParam param) {
        foodQueryRepository.findById(foodId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FoodStory story = FoodStory.createPlace(foodId, 1L, param.getName(), param.getContent(), param.getCategory(), param.getAddress(), param.getNaverUrl());
        FoodStory savedStory = foodCommandRepository.saveStory(story);

        List<FoodStoryImage> images = createImages(savedStory.getId(), param.getImageUrls());
        List<FoodStoryImage> savedImages = foodCommandRepository.saveAllImages(images);

        return FoodStoryServiceDto.from(savedStory, savedImages);
    }

    private List<FoodStoryImage> createImages(Long storyId, List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return List.of();
        }
        return IntStream.range(0, imageUrls.size())
                .mapToObj(i -> FoodStoryImage.create(storyId, imageUrls.get(i), i))
                .toList();
    }
}
