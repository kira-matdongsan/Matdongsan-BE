package com.example.matdongsan.food.application.dto;

import com.example.matdongsan.food.domain.FoodStoryImage;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FoodStoryImageServiceDto {

    private final Long id;
    private final String imageUrl;
    private final String thumbnailUrl;
    private final Integer orderNum;

    public static FoodStoryImageServiceDto from(FoodStoryImage image) {
        return FoodStoryImageServiceDto.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .thumbnailUrl(image.getThumbnailUrl())
                .orderNum(image.getOrderNum())
                .build();
    }
}
