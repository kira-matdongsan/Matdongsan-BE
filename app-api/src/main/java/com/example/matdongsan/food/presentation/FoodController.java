package com.example.matdongsan.food.presentation;

import com.example.matdongsan.response.RestApiResponse;
import com.example.matdongsan.response.PageResponse;
import com.example.matdongsan.response.ResponseCode;
import com.example.matdongsan.response.ResultResponse;
import com.example.matdongsan.food.application.dto.FoodStoryServiceDto;
import com.example.matdongsan.food.presentation.response.DishPickResponse;
import com.example.matdongsan.food.presentation.request.DishRequest;
import com.example.matdongsan.food.presentation.response.FoodInfoResponse;
import com.example.matdongsan.food.presentation.response.StoryResponse;
import com.example.matdongsan.dish.application.service.DishService;
import com.example.matdongsan.food.application.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.matdongsan.common.util.auth.CurrentUser;
import com.example.matdongsan.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "제철 음식 상세 API", description = "제철 음식 상세 페이지 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/foods")
public class FoodController {

    private final FoodService foodService;
    private final DishService dishService;

    @Operation(summary = "정보 조회", description = "제철 음식 ID로 정보 조회")
    @GetMapping("/{id}/info")
    public ResponseEntity<RestApiResponse<FoodInfoResponse>> getFoodInfoById(
            @Parameter(name = "id", description = "조회할 제철 음식 ID", example = "1")
            @PathVariable Long id
    ) {
        return RestApiResponse.success(ResponseCode.OK, FoodInfoResponse.from(foodService.getFoodInfoById(id), true));
    }

    @Operation(summary = "맛동산 Pick 제철요리 목록 조회", description = "제철 음식 ID로 제철요리 조회")
    @GetMapping("/{id}/dishes")
    public ResponseEntity<RestApiResponse<DishPickResponse>> getAllDishesByFoodId(
            @Parameter(name = "id", description = "조회할 제철 음식 ID", example = "1")
            @PathVariable Long id
    ) {
        return RestApiResponse.success(ResponseCode.OK, DishPickResponse.from(foodService.getAllDishesByFoodId(id)));
    }

    @Operation(summary = "맛동산 Pick 제철요리 등록 및 투표", description = "이미지를 등록하여 제철 요리 등록 및 투표")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/dishes")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> createDish(
            @Parameter(name = "id", description = "제철 요리를 등록할 제철 음식 ID", example = "1")
            @PathVariable Long id,
            @CurrentUser User user,
            @RequestBody DishRequest requestDto
    ) {
        dishService.createDish(id, user.getId(), requestDto.toParam());
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

    @Operation(summary = "나의 제철음식 이야기 목록 조회", description = "제철 음식 ID로 제철음식 이야기 조회")
    @GetMapping("/{id}/stories")
    public ResponseEntity<RestApiResponse<PageResponse<StoryResponse>>> getAllStoriesByFoodId(
            @Parameter(name = "id", description = "조회할 제철 음식 ID", example = "1") @PathVariable Long id,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<StoryResponse> storyResponses = foodService.getAllStoriesByFoodId(id, pageable)
                .map(dto -> StoryResponse.from(dto, true));
        return RestApiResponse.successPage(ResponseCode.OK, storyResponses);
    }

    @Operation(summary = "제철 음식 좋아요 추가/취소", description = "제철 음식 ID로 제철음식 좋아요/취소")
    @PostMapping("/{id}/like")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> likeFood(
            @Parameter(name = "id", description = "조회할 제철 음식 ID", example = "1")
            @PathVariable Long id
    ) {
        foodService.likeFood(id);
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

}
