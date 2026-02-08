package com.example.matdongsan.food.domain;

import com.example.matdongsan.food.enums.FoodStoryType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class FoodStory {

    private Long id;
    private Long foodId;
    private Long userId;
    private FoodStoryType type;
    private Integer likeCount;
    private Integer reportCount;

    // SeasonalNote fields
    private String content;
    private LocalDate recordedDate;

    // Recipe fields
    private String recipeName;
    private String ingredients;
    private String instructions;

    // Place fields
    private String placeName;
    private String placeContent;
    private String category;
    private String address;
    private String naverUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static FoodStory createSeasonalNote(Long foodId, Long userId, String content, LocalDate recordedDate) {
        return FoodStory.builder()
                .foodId(foodId)
                .userId(userId)
                .type(FoodStoryType.SEASONAL_NOTE)
                .content(content)
                .recordedDate(recordedDate)
                .likeCount(0)
                .reportCount(0)
                .build();
    }

    public static FoodStory createRecipe(Long foodId, Long userId, String recipeName, String ingredients, String instructions) {
        return FoodStory.builder()
                .foodId(foodId)
                .userId(userId)
                .type(FoodStoryType.RECIPE)
                .recipeName(recipeName)
                .ingredients(ingredients)
                .instructions(instructions)
                .likeCount(0)
                .reportCount(0)
                .build();
    }

    public static FoodStory createPlace(Long foodId, Long userId, String placeName, String placeContent, String category, String address, String naverUrl) {
        return FoodStory.builder()
                .foodId(foodId)
                .userId(userId)
                .type(FoodStoryType.PLACE)
                .placeName(placeName)
                .placeContent(placeContent)
                .category(category)
                .address(address)
                .naverUrl(naverUrl)
                .likeCount(0)
                .reportCount(0)
                .build();
    }
}
