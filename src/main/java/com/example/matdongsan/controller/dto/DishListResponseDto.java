package com.example.matdongsan.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "맛동산 Pick 제철요리 목록 응답 DTO")
@Builder
@Getter
public class DishListResponseDto {

    @Schema(description = "제철요리 ID", example = "1")
    private final Long id;

    @Schema(description = "제철요리 이름", example = "콘치즈")
    private final String name;

    @Schema(description = "제철요리 썸네일 이미지", example = "1")
    private final String thumbnailUrl;

    @Schema(description = "제철요리 순위", example = "1")
    private final Integer rank;

    @Schema(description = "제철요리 투표 수", example = "13")
    private final Integer voteCount;

}
