package com.example.matdongsan.controller.dto;

import com.example.matdongsan.domain.FoodStoryImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "제철음식 이야기 이미지 응답 DTO")
@Builder
@Getter
public class StoryImageResponseDto {

    @Schema(description = "원본 이미지 URL", example = "")
    private final String imageUrl;

    @Schema(description = "썸네일 이미지 URL", example = "")
    private final String thumbnailUrl;

    public static StoryImageResponseDto of(FoodStoryImage foodStoryImage) {
        return StoryImageResponseDto.builder()
                .imageUrl(foodStoryImage.getImageUrl())
                .thumbnailUrl(foodStoryImage.getThumbnailUrl())
                .build();
    }

}
