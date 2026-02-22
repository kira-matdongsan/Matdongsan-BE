package com.example.matdongsan.dish.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Dish {

    private Long id;
    private Long featuredFoodId;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static Dish create(Long featuredFoodId, String name) {
        LocalDateTime now = LocalDateTime.now();
        return Dish.builder()
                .featuredFoodId(featuredFoodId)
                .name(name)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
