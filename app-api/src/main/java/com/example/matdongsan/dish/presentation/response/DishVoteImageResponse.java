package com.example.matdongsan.dish.presentation.response;

import com.example.matdongsan.dish.application.dto.DishVoteImageServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "제철요리 투표 이미지 목록 응답 DTO")
@Builder
@Getter
public class DishVoteImageResponse {

    @Schema(description = "투표 이미지 ID", example = "")
    private final Long imageId;

    @Schema(description = "투표 이미지 URL", example = "")
    private final String imageUrl;

    public static DishVoteImageResponse from(DishVoteImageServiceDto dto) {
        return DishVoteImageResponse.builder()
                .imageId(dto.getId())
                .imageUrl(dto.getImageUrl())
                .build();
    }
}
