package com.example.matdongsan.dish.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DishVoteImage {

    private Long id;
    private Long dishId;
    private Long dishVoteId;
    private String imageUrl;
    private String thumbnailUrl;
    private Integer orderNum;
    private Integer reportCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static DishVoteImage create(Long dishId, String imageUrl, int orderNum) {
        return DishVoteImage.builder()
                .dishId(dishId)
                .imageUrl(imageUrl)
                .orderNum(orderNum)
                .reportCount(0)
                .build();
    }
}
