package com.example.matdongsan.controller.dto;

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

}
