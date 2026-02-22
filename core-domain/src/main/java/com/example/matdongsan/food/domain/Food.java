package com.example.matdongsan.food.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class Food {

    private Long id;
    private String name;
    private String englishName;
    private String imageUrl;
    private String thumbnailUrl;
    private String color;
    private String subtitle;
    private String description;
    private List<Integer> seasonMonths;
    private String regions;
    private String benefits;
    private String buyingTips;
    private String preparationTips;
    private Map<String, Object> nutrients;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
