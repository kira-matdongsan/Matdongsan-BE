package com.example.matdongsan.food.presentation.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Schema(description = "제철 음식 영양성분 응답 DTO")
@Builder
@Getter
public class FoodNutrientResponse {

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

    public static FoodNutrientResponse of(Map<String, Object> nutrients) {
        if (nutrients == null) return null;
        return FoodNutrientResponse.builder()
                .servingSizeLabel((String) nutrients.get("servingSizeLabel"))
                .servingSizeGram(toInteger(nutrients.get("servingSizeGram")))
                .calories(toFloat(nutrients.get("calories")))
                .carbohydrate(toFloat(nutrients.get("carbohydrate")))
                .dietaryFiber(toFloat(nutrients.get("dietaryFiber")))
                .sugars(toFloat(nutrients.get("sugars")))
                .protein(toFloat(nutrients.get("protein")))
                .fat(toFloat(nutrients.get("fat")))
                .build();
    }

    private static Float toFloat(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return n.floatValue();
        return Float.parseFloat(value.toString());
    }

    private static Integer toInteger(Object value) {
        if (value == null) return null;
        if (value instanceof Number n) return n.intValue();
        return Integer.parseInt(value.toString());
    }
}
