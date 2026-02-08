package com.example.matdongsan.food.application.dto;

import com.example.matdongsan.food.domain.FeaturedFood;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class FeaturedFoodServiceDto {

    private final Long id;
    private final Long foodId;
    private final Integer year;
    private final Integer week;
    private final Integer dishVoteCount;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    public static FeaturedFoodServiceDto from(FeaturedFood featuredFood) {
        return FeaturedFoodServiceDto.builder()
                .id(featuredFood.getId())
                .foodId(featuredFood.getFoodId())
                .year(featuredFood.getYear())
                .week(featuredFood.getWeek())
                .dishVoteCount(featuredFood.getDishVoteCount())
                .startAt(featuredFood.getStartAt())
                .endAt(featuredFood.getEndAt())
                .build();
    }
}
