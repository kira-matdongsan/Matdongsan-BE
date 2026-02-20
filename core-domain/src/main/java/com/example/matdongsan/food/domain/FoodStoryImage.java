package com.example.matdongsan.food.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FoodStoryImage {

    private Long id;
    private Long foodStoryId;
    private String imageUrl;
    private String thumbnailUrl;
    private Integer orderNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static FoodStoryImage create(Long foodStoryId, String imageUrl, int orderNum) {
        return FoodStoryImage.builder()
                .foodStoryId(foodStoryId)
                .imageUrl(imageUrl)
                .thumbnailUrl(imageUrl)
                .orderNum(orderNum)
                .build();
    }
}
