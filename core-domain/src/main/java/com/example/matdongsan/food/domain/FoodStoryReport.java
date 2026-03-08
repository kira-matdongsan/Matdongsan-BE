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
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static FoodStoryReport create(Long foodStoryId, Long userId, String reason) {
        return FoodStoryReport.builder()
                .foodStoryId(foodStoryId)
                .userId(userId)
                .reason(reason)
                .build();
    }
}
