package com.example.matdongsan.home.presentation.response;

import com.example.matdongsan.home.application.dto.HomeServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "홈 화면 응답 DTO")
@Builder
@Getter
public class HomeResponse {

    @Schema(description = "배너 정보")
    private final BannerResponse banner;

    @Schema(description = "현재 제철 음식 정보")
    private final FeaturedFoodResponse featuredFood;

    public static HomeResponse from(HomeServiceDto dto) {
        return HomeResponse.builder()
                .banner(BannerResponse.builder()
                        .weekText(dto.getWeekText())
                        .build())
                .featuredFood(FeaturedFoodResponse.builder()
                        .foodId(dto.getFoodId())
                        .name(dto.getName())
                        .subtitle(dto.getSubtitle())
                        .thumbnail(dto.getThumbnail())
                        .build())
                .build();
    }

    @Schema(description = "배너 응답")
    @Builder
    @Getter
    public static class BannerResponse {

        @Schema(description = "주차 텍스트", example = "6월 셋째주")
        private final String weekText;
    }

    @Schema(description = "제철 음식 응답")
    @Builder
    @Getter
    public static class FeaturedFoodResponse {

        @Schema(description = "제철 음식 ID", example = "170")
        private final Long foodId;

        @Schema(description = "제철 음식명", example = "딸기")
        private final String name;

        @Schema(description = "제철 음식 소제목", example = "꼭지부터 끝부분까지 달콤함으로 꽉 찬")
        private final String subtitle;

        @Schema(description = "제철 음식 썸네일 URL")
        private final String thumbnail;
    }
}
