package com.example.matdongsan.food.presentation.response;

import com.example.matdongsan.food.application.dto.FoodServiceDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Schema(description = "제철 음식 상세 응답 DTO")
@Builder
@Getter
public class FoodInfoResponse {

    @Schema(description = "주차 텍스트", example = "2026년 2월 셋째주")
    private final String weekText;

    @Schema(description = "제철 음식 ID", example = "1")
    private final Long id;

    @Schema(description = "제철 음식명", example = "옥수수")
    private final String name;

    @Schema(description = "제철 음식 영문명", example = "Corn")
    private final String englishName;

    @Schema(description = "제철 음식 소제목", example = "알맹이부터 수염까지 아낌없이 주는")
    private final String subtitle;

    @Schema(description = "제철 음식 설명", example = "맛동산에서 7월 둘째주로 선정한 제철음식은 바로 '옥수수' 입니다.")
    private final String description;

    @Schema(description = "제철 음식 대표 이미지 URL", example = "")
    private final String imageUrl;

    @Schema(description = "제철 음식 대표 컬러", example = "#FFEB7B")
    private final String color;

    @Schema(description = "현재 특집 선정 여부", example = "true")
    private final Boolean isFeatured;

    @Schema(description = "마지막 제철 음식 특집 선정일", example = "2025-07-07")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate lastFeaturedDate;

    @Schema(description = "제철 음식 좋아요 여부", example = "true")
    private final Boolean isLiked;

    @Schema(description = "제철 음식 제철시기", example = "7~8월")
    private final String seasonMonths;

    @Schema(description = "제철 음식 주요산지", example = "강원 홍천, 영원, 평창, 충북 괴산, 전남 등지")
    private final String regions;

    @Schema(description = "제철 음식 효능", example = "비타민B1, B2, E와 함께 칼륨, 철분 등 무기질이 풍부합니다.")
    private final String benefits;

    @Schema(description = "제철 음식 구입요령", example = "찰옥수수는 겉껍질이 푸르고 윤기가 나며, 알맹이가 꽉 차 있는 것이 좋습니다.")
    private final String buyingTips;

    @Schema(description = "제철 음식 손질요령", example = "찰옥수수의 껍질과 수염을 같이 잡고 아래로 세게 벗긴다.")
    private final String preparationTips;

    @Schema(description = "제철 음식 영양성분")
    private final FoodNutrientResponse nutrients;

    public static FoodInfoResponse from(FoodServiceDto dto, Boolean isLiked) {
        return FoodInfoResponse.builder()
                .weekText(dto.getWeekText())
                .id(dto.getId())
                .name(dto.getName())
                .englishName(dto.getEnglishName())
                .subtitle(dto.getSubtitle())
                .description(dto.getDescription())
                .imageUrl(dto.getImageUrl())
                .color(dto.getColor())
                .isFeatured(dto.getIsFeatured())
                .lastFeaturedDate(dto.getLastFeaturedAt() != null ? dto.getLastFeaturedAt().toLocalDate() : null)
                .isLiked(isLiked)
                .seasonMonths(formatSeasonMonths(dto.getSeasonMonths()))
                .regions(dto.getRegions())
                .benefits(dto.getBenefits())
                .buyingTips(dto.getBuyingTips())
                .preparationTips(dto.getPreparationTips())
                .nutrients(FoodNutrientResponse.of(dto.getNutrients()))
                .build();
    }

    private static String formatSeasonMonths(List<Integer> months) {
        if (months == null || months.isEmpty()) return null;
        int min = Collections.min(months);
        int max = Collections.max(months);
        return min == max ? min + "월" : min + "~" + max + "월";
    }
}
