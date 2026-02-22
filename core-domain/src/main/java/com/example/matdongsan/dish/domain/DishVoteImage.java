package com.example.matdongsan.dish.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class DishVoteImage {

    private Long id;
    private Long dishVoteId;
    private String imageUrl;
    private String thumbnailUrl;
    private Integer orderNum;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static DishVoteImage create(String imageUrl, int orderNum) {
        return DishVoteImage.builder()
                .imageUrl(imageUrl)
                .orderNum(orderNum)
                .build();
    }
}
