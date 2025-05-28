package com.example.matdongsan.controller;

import com.example.matdongsan.common.CommonResponse;
import com.example.matdongsan.common.PageResponse;
import com.example.matdongsan.common.ResponseCode;
import com.example.matdongsan.common.ResultResponse;
import com.example.matdongsan.controller.dto.DishResponseDto;
import com.example.matdongsan.controller.dto.FoodInfoResponseDto;
import com.example.matdongsan.controller.dto.StoryResponseDto;
import com.example.matdongsan.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "제철 음식 상세 API", description = "제철 음식 상세 페이지 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/foods")
public class FoodController {

    private final FoodService foodService;

    @Operation(summary = "정보 조회", description = "제철 음식 ID로 정보 조회")
    @GetMapping("/{id}/info")
    public ResponseEntity<CommonResponse<FoodInfoResponseDto>> getFoodInfoById(
            @Parameter(name = "id", description = "조회할 제철 음식 ID", example = "1")
            @PathVariable Long id
    ) {
        return CommonResponse.success(ResponseCode.OK, foodService.getFoodInfoById(id));
    }

    @Operation(summary = "맛동산 Pick 제철요리 목록 조회", description = "제철 음식 ID로 제철요리 조회")
    @GetMapping("/{id}/dishes")
    public ResponseEntity<CommonResponse<DishResponseDto>> getAllDishesByFoodId(
            @Parameter(name = "id", description = "조회할 제철 음식 ID", example = "1")
            @PathVariable Long id
    ) {
        return CommonResponse.success(ResponseCode.OK, foodService.getAllDishesByFoodId(id));
    }

    @Operation(summary = "나의 제철음식 이야기 목록 조회", description = "제철 음식 ID로 제철음식 이야기 조회")
    @GetMapping("/{id}/stories")
    public ResponseEntity<CommonResponse<PageResponse<StoryResponseDto>>> getAllStoriesByFoodId(
            @Parameter(name = "id", description = "조회할 제철 음식 ID", example = "1") @PathVariable Long id,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return CommonResponse.successPage(ResponseCode.OK, foodService.getAllStoriesByFoodId(id, pageable));
    }

    @Operation(summary = "제철 음식 좋아요 추가/취소", description = "제철 음식 ID로 제철음식 좋아요/취소")
    @PostMapping("/{id}/like")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> likeFood(
            @Parameter(name = "id", description = "조회할 제철 음식 ID", example = "1")
            @PathVariable Long id
    ) {
        foodService.likeFood(id);
        return CommonResponse.successResult(ResponseCode.OK, true);
    }

}
