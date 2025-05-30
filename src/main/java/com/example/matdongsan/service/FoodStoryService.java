package com.example.matdongsan.service;

import com.example.matdongsan.common.ErrorCode;
import com.example.matdongsan.common.exception.CustomException;
import com.example.matdongsan.common.util.service.S3Uploader;
import com.example.matdongsan.controller.dto.StoryImageResponseDto;
import com.example.matdongsan.controller.dto.StoryResponseDto;
import com.example.matdongsan.domain.Food;
import com.example.matdongsan.domain.FoodStoryImage;
import com.example.matdongsan.domain.FoodStorySeasonalNote;
import com.example.matdongsan.repository.*;
import com.example.matdongsan.service.dto.PlaceServiceDto;
import com.example.matdongsan.service.dto.RecipeServiceDto;
import com.example.matdongsan.service.dto.SeasonalNoteServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FoodStoryService {

    private final S3Uploader s3Uploader;
    private final FoodRepository foodRepository;
    private final FoodStoryRepository foodStoryRepository;
    private final FoodStorySeasonalNoteRepository foodStorySeasonalNoteRepository;
    private final FoodStoryRecipeRepository foodStoryRecipeRepository;
    private final FoodStoryPlaceRepository foodStoryPlaceRepository;
    private final FoodStoryImageRepository foodStoryImageRepository;

    @Transactional
    public StoryResponseDto createSeasonalNoteStory(Long foodId, SeasonalNoteServiceDto serviceDto, List<MultipartFile> imageFiles) throws IOException {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));
        FoodStorySeasonalNote story = foodStorySeasonalNoteRepository.save(serviceDto.toFoodStorySeasonalNote(food));

        List<String> imageUrls = s3Uploader.uploadAll(imageFiles, "food-story/" + story.getId());

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

    public StoryResponseDto createRecipeStory(Long foodId, RecipeServiceDto serviceDto) {
        return StoryResponseDto.builder().build();
    }

    public StoryResponseDto createPlaceStory(Long foodId, PlaceServiceDto serviceDto) {
        return StoryResponseDto.builder().build();
    }
}
