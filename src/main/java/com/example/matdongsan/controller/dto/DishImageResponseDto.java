package com.example.matdongsan.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "제철요리 이미지 목록 응답 DTO")
@Builder
@Getter
public class DishImageResponseDto {

    @Schema(description = "제철요리 이미지 ID", example = "")
    private final Long imageId;

    @Schema(description = "제철요리 이미지 URL", example = "")
    private final String imageUrl;
}
