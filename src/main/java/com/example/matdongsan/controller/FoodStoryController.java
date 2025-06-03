package com.example.matdongsan.controller;

import com.example.matdongsan.common.CommonResponse;
import com.example.matdongsan.common.ResponseCode;
import com.example.matdongsan.controller.dto.PlaceCreateRequestDto;
import com.example.matdongsan.controller.dto.RecipeCreateRequestDto;
import com.example.matdongsan.controller.dto.SeasonalNoteCreateRequestDto;
import com.example.matdongsan.controller.dto.StoryResponseDto;
import com.example.matdongsan.service.FoodStoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "나의 제철음식 이야기 API", description = "나의 제철음식 이야기 관련 API")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/foods")
public class FoodStoryController {

    private final FoodStoryService foodStoryService;

    // 이야기 작성
    @Operation(summary = "제철기록 작성", description = "제철 음식 ID로 제철음식 이야기 작성")
    @PostMapping("/{id}/stories/seasonal-note")
    public ResponseEntity<CommonResponse<StoryResponseDto>> createSeasonalNoteStory(
            @Parameter(name = "id", description = "이야기를 작성할 제철 음식 ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid SeasonalNoteCreateRequestDto request
    ) {
        return CommonResponse.success(ResponseCode.OK, foodStoryService.createSeasonalNoteStory(id, request.toServiceDto()));
    }

    @Operation(summary = "레시피 작성", description = "제철 음식 ID로 제철음식 이야기 작성")
    @PostMapping("/{id}/stories/recipe")
    public ResponseEntity<CommonResponse<StoryResponseDto>> createRecipeStory(
            @Parameter(name = "id", description = "이야기를 작성할 제철 음식 ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid RecipeCreateRequestDto request
    ) {
        return CommonResponse.success(ResponseCode.OK, foodStoryService.createRecipeStory(id, request.toServiceDto()));
    }

    @Operation(summary = "플레이스 작성", description = "제철 음식 ID로 제철음식 이야기 작성")
    @PostMapping("/{id}/stories/place")
    public ResponseEntity<CommonResponse<StoryResponseDto>> createPlaceStory(
            @Parameter(name = "id", description = "이야기를 작성할 제철 음식 ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid PlaceCreateRequestDto request
    ) {
        return CommonResponse.success(ResponseCode.OK, foodStoryService.createPlaceStory(id, request.toServiceDto()));
    }

    // 이야기 수정

    // 이야기 삭제

    // 이야기 신고

    // 사용자 차단
}
