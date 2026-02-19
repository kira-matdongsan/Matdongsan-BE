package com.example.matdongsan.food.application.dto;

import com.example.matdongsan.food.domain.FoodStory;
import com.example.matdongsan.food.domain.FoodStoryImage;
import com.example.matdongsan.food.enums.FoodStoryType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class FoodStoryServiceDto {

    private final Long id;
    private final Long foodId;
    private final Long userId;
    private final FoodStoryType type;
    private final Integer likeCount;
    private final Integer reportCount;

    // SeasonalNote
    private final String content;
    private final LocalDate recordedDate;

    // Recipe
    private final String recipeName;
    private final String ingredients;
    private final String instructions;

    // Place
    private final String placeName;
    private final String placeContent;
    private final String category;
    private final String address;
    private final String naverUrl;

    private final String nickname;
    private final String profileImageUrl;
    private final List<FoodStoryImageServiceDto> images;
    private final LocalDateTime createdAt;

    public static FoodStoryServiceDto from(FoodStory story, List<FoodStoryImage> images, String nickname, String profileImageUrl) {
        return FoodStoryServiceDto.builder()
                .id(story.getId())
                .foodId(story.getFoodId())
                .userId(story.getUserId())
                .type(story.getType())
                .likeCount(story.getLikeCount())
                .reportCount(story.getReportCount())
                .content(story.getContent())
                .recordedDate(story.getRecordedDate())
                .recipeName(story.getRecipeName())
                .ingredients(story.getIngredients())
                .instructions(story.getInstructions())
                .placeName(story.getPlaceName())
                .placeContent(story.getPlaceContent())
                .category(story.getCategory())
                .address(story.getAddress())
                .naverUrl(story.getNaverUrl())
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .images(images.stream().map(FoodStoryImageServiceDto::from).toList())
                .createdAt(story.getCreatedAt())
                .build();
    }
}
