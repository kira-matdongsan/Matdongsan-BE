package com.example.matdongsan.dish.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Dish {

    private Long id;
    private Long foodId;
    private Long featuredFoodId;
    private String name;
    private Integer voteCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static Dish create(Long foodId, Long featuredFoodId, String name) {
        LocalDateTime now = LocalDateTime.now();
        return Dish.builder()
                .foodId(foodId)
                .featuredFoodId(featuredFoodId)
                .name(name)
                .voteCount(1)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void plusVoteCount() {
        this.voteCount++;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
