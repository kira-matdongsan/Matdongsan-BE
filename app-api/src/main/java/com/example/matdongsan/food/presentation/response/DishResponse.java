package com.example.matdongsan.food.presentation.response;

import com.example.matdongsan.food.application.dto.DishServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "맛동산 Pick 제철요리 목록 응답 DTO")
@Builder
@Getter
public class DishResponse {

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

    public static DishResponse from(DishServiceDto dto) {
        return DishResponse.builder()
                .id(dto.getId())
                .name(dto.getName())
                .thumbnailUrl(dto.getThumbnailUrl())
                .rank(dto.getRank())
                .voteCount(dto.getVoteCount())
                .build();
    }
}
