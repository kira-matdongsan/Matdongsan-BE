package com.example.matdongsan.food.presentation.response;

import com.example.matdongsan.food.application.dto.FoodStoryImageServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "제철음식 이야기 이미지 응답 DTO")
@Builder
@Getter
public class StoryImageResponse {

    @Schema(description = "원본 이미지 URL", example = "")
    private final String imageUrl;

    @Schema(description = "썸네일 이미지 URL", example = "")
    private final String thumbnailUrl;

    public static StoryImageResponse from(FoodStoryImageServiceDto dto) {
        return StoryImageResponse.builder()
                .imageUrl(dto.getImageUrl())
                .thumbnailUrl(dto.getThumbnailUrl())
                .build();
    }

}
