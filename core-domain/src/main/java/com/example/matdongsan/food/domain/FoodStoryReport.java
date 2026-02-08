package com.example.matdongsan.food.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FoodStoryReport {

    private Long id;
    private Long foodStoryId;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static FoodStoryReport create(Long foodStoryId, Long userId) {
        return FoodStoryReport.builder()
                .foodStoryId(foodStoryId)
                .userId(userId)
                .build();
    }
}
