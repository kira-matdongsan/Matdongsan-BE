package com.example.matdongsan.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "제철 음식 영양성분 응답 DTO")
@Builder
@Getter
public class FoodNutrientResponseDto {

    @Schema(description = "1회 제공량", example = "100g")
    private final String servingSizeLabel;

    @Schema(description = "1회 제공량 g기준 (계산용)", example = "100")
    private final Integer servingSizeGram;

    @Schema(description = "에너지 (g)", example = "82")
    private final Float calories;

    @Schema(description = "탄수화물 (g)", example = "19")
    private final Float carbohydrate;

    @Schema(description = "식이섬유 (g)", example = "2.7")
    private final Float dietaryFiber;

    @Schema(description = "당 (g)", example = "3.2")
    private final Float sugars;

    @Schema(description = "단백질 (g)", example = "6.9")
    private final Float protein;

    @Schema(description = "지방 (g)", example = "1.2")
    private final Float fat;

}
