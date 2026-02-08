package com.example.matdongsan.food.application.dto;

import com.example.matdongsan.food.domain.Food;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class FoodServiceDto {

    private final Long id;
    private final String name;
    private final String englishName;
    private final Boolean isFeatured;
    private final LocalDateTime lastFeaturedAt;
    private final String imageUrl;
    private final String thumbnailUrl;
    private final String color;
    private final String subtitle;
    private final String description;
    private final Integer likeCount;
    private final List<Integer> seasonMonths;
    private final String regions;
    private final String benefits;
    private final String buyingTips;
    private final String preparationTips;
    private final Map<String, Object> nutrients;

    public static FoodServiceDto from(Food food) {
        return FoodServiceDto.builder()
                .id(food.getId())
                .name(food.getName())
                .englishName(food.getEnglishName())
                .isFeatured(food.getIsFeatured())
                .lastFeaturedAt(food.getLastFeaturedAt())
                .imageUrl(food.getImageUrl())
                .thumbnailUrl(food.getThumbnailUrl())
                .color(food.getColor())
                .subtitle(food.getSubtitle())
                .description(food.getDescription())
                .likeCount(food.getLikeCount())
                .seasonMonths(food.getSeasonMonths())
                .regions(food.getRegions())
                .benefits(food.getBenefits())
                .buyingTips(food.getBuyingTips())
                .preparationTips(food.getPreparationTips())
                .nutrients(food.getNutrients())
                .build();
    }
}
