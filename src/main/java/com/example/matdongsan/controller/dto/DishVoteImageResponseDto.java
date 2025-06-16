package com.example.matdongsan.controller.dto;

import com.example.matdongsan.domain.DishVoteImage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "제철요리 투표 이미지 목록 응답 DTO")
@Builder
@Getter
public class DishVoteImageResponseDto {

    @Schema(description = "투표 이미지 ID", example = "")
    private final Long imageId;

    @Schema(description = "투표 이미지 URL", example = "")
    private final String imageUrl;

    public static DishVoteImageResponseDto of (DishVoteImage dishVoteImage) {
        return DishVoteImageResponseDto.builder()
                .imageId(dishVoteImage.getId())
                .imageUrl(dishVoteImage.getImageUrl())
                .build();
    }
}
