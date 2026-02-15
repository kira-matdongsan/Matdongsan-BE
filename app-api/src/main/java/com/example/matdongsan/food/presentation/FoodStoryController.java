package com.example.matdongsan.food.presentation;

import com.example.matdongsan.common.util.auth.CurrentUser;
import com.example.matdongsan.response.RestApiResponse;
import com.example.matdongsan.response.ResponseCode;
import com.example.matdongsan.response.ResultResponse;
import com.example.matdongsan.user.domain.User;
import com.example.matdongsan.food.presentation.request.PlaceCreateRequest;
import com.example.matdongsan.food.presentation.request.RecipeCreateRequest;
import com.example.matdongsan.food.presentation.request.SeasonalNoteCreateRequest;
import com.example.matdongsan.food.presentation.response.StoryResponse;
import com.example.matdongsan.food.application.service.FoodStoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/stories/seasonal-note")
    public ResponseEntity<RestApiResponse<StoryResponse>> createSeasonalNoteStory(
            @Parameter(name = "id", description = "이야기를 작성할 제철 음식 ID", example = "1")
            @PathVariable Long id,
            @CurrentUser User user,
            @RequestBody @Valid SeasonalNoteCreateRequest request
    ) {
        return RestApiResponse.success(ResponseCode.OK, StoryResponse.from(foodStoryService.createSeasonalNoteStory(id, user.getId(), request.toParam()), true));
    }

    @Operation(summary = "레시피 작성", description = "제철 음식 ID로 제철음식 이야기 작성")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/stories/recipe")
    public ResponseEntity<RestApiResponse<StoryResponse>> createRecipeStory(
            @Parameter(name = "id", description = "이야기를 작성할 제철 음식 ID", example = "1")
            @PathVariable Long id,
            @CurrentUser User user,
            @RequestBody @Valid RecipeCreateRequest request
    ) {
        return RestApiResponse.success(ResponseCode.OK, StoryResponse.from(foodStoryService.createRecipeStory(id, user.getId(), request.toParam()), true));
    }

    @Operation(summary = "플레이스 작성", description = "제철 음식 ID로 제철음식 이야기 작성")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/stories/place")
    public ResponseEntity<RestApiResponse<StoryResponse>> createPlaceStory(
            @Parameter(name = "id", description = "이야기를 작성할 제철 음식 ID", example = "1")
            @PathVariable Long id,
            @CurrentUser User user,
            @RequestBody @Valid PlaceCreateRequest request
    ) {
        return RestApiResponse.success(ResponseCode.OK, StoryResponse.from(foodStoryService.createPlaceStory(id, user.getId(), request.toParam()), true));
    }

    // 이야기 수정

    @Operation(summary = "이야기 삭제", description = "본인이 작성한 제철음식 이야기 삭제")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/stories/{storyId}")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> deleteStory(
            @Parameter(name = "storyId", description = "삭제할 이야기 ID", example = "1")
            @PathVariable Long storyId,
            @CurrentUser User user
    ) {
        foodStoryService.deleteStory(storyId, user.getId());
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

    // 이야기 신고

    // 사용자 차단
}
