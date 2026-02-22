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
import com.example.matdongsan.user.domain.UserProfile;
import com.example.matdongsan.user.repository.UserQueryRepository;
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
    private final UserQueryRepository userQueryRepository;

    @Transactional
    public FoodStoryServiceDto createSeasonalNoteStory(Long foodId, Long userId, CreateSeasonalNoteParam param) {
        foodQueryRepository.findById(foodId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FoodStory story = FoodStory.createSeasonalNote(foodId, userId, param.getContent(), param.getRecordedDate());
        FoodStory savedStory = foodCommandRepository.saveStory(story);

        List<FoodStoryImage> images = createImages(savedStory.getId(), param.getImageUrls());
        List<FoodStoryImage> savedImages = foodCommandRepository.saveAllImages(images);

        UserProfile profile = userQueryRepository.findProfileByUserId(story.getUserId()).orElse(null);
        String nickname = (profile != null && profile.getNickname() != null) ? profile.getNickname() : "도란도란";
        String profileImageUrl = (profile != null && profile.getProfileImageUrl() != null) ? profile.getProfileImageUrl() : "https://matdongsan-dev-bucket.s3.ap-northeast-2.amazonaws.com/public/profile-image/default.png";
        return FoodStoryServiceDto.from(savedStory, savedImages, 0, nickname, profileImageUrl);
    }

    @Transactional
    public FoodStoryServiceDto createRecipeStory(Long foodId, Long userId, CreateRecipeParam param) {
        foodQueryRepository.findById(foodId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FoodStory story = FoodStory.createRecipe(foodId, userId, param.getName(), param.getIngredients(), param.getInstructions());
        FoodStory savedStory = foodCommandRepository.saveStory(story);

        List<FoodStoryImage> images = createImages(savedStory.getId(), param.getImageUrls());
        List<FoodStoryImage> savedImages = foodCommandRepository.saveAllImages(images);

        UserProfile profile = userQueryRepository.findProfileByUserId(story.getUserId()).orElse(null);
        String nickname = (profile != null && profile.getNickname() != null) ? profile.getNickname() : "도란도란";
        String profileImageUrl = (profile != null && profile.getProfileImageUrl() != null) ? profile.getProfileImageUrl() : "https://matdongsan-dev-bucket.s3.ap-northeast-2.amazonaws.com/public/profile-image/default.png";
        return FoodStoryServiceDto.from(savedStory, savedImages, 0, nickname, profileImageUrl);
    }

    @Transactional
    public FoodStoryServiceDto createPlaceStory(Long foodId, Long userId, CreatePlaceParam param) {
        foodQueryRepository.findById(foodId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_NOT_FOUND));

        FoodStory story = FoodStory.createPlace(foodId, userId, param.getName(), param.getContent(), param.getCategory(), param.getAddress(), param.getNaverUrl());
        FoodStory savedStory = foodCommandRepository.saveStory(story);

        List<FoodStoryImage> images = createImages(savedStory.getId(), param.getImageUrls());
        List<FoodStoryImage> savedImages = foodCommandRepository.saveAllImages(images);

        UserProfile profile = userQueryRepository.findProfileByUserId(story.getUserId()).orElse(null);
        String nickname = (profile != null && profile.getNickname() != null) ? profile.getNickname() : "도란도란";
        String profileImageUrl = (profile != null && profile.getProfileImageUrl() != null) ? profile.getProfileImageUrl() : "https://matdongsan-dev-bucket.s3.ap-northeast-2.amazonaws.com/public/profile-image/default.png";
        return FoodStoryServiceDto.from(savedStory, savedImages, 0, nickname, profileImageUrl);
    }

    @Transactional
    public void deleteStory(Long storyId, Long userId) {
        FoodStory story = foodQueryRepository.findStoryById(storyId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORY_NOT_FOUND));

        if (!story.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.STORY_NOT_OWNER);
        }

        foodCommandRepository.deleteStory(storyId);
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
