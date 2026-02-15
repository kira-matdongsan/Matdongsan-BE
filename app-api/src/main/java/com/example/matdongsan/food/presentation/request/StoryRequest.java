package com.example.matdongsan.food.presentation.request;

import com.example.matdongsan.food.application.dto.StoryParam;
import com.example.matdongsan.food.enums.FoodStoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "제철음식 이야기 목록 조회 요청")
@Getter
@Setter
public class StoryRequest {

    @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
    private int page = 0;

    @Schema(description = "페이지 크기", example = "10")
    private int size = 10;

    @Schema(description = "이야기 유형 필터 (미입력시 전체 조회)", example = "RECIPE")
    private FoodStoryType type;

    public StoryParam toParam() {
        return StoryParam.builder()
                .page(page)
                .size(size)
                .type(type)
                .build();
    }
}
