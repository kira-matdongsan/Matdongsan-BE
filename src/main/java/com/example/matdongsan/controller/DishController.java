package com.example.matdongsan.controller;

import com.example.matdongsan.common.CommonResponse;
import com.example.matdongsan.common.ListResponse;
import com.example.matdongsan.common.ResponseCode;
import com.example.matdongsan.common.ResultResponse;
import com.example.matdongsan.controller.dto.DishImageResponseDto;
import com.example.matdongsan.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "맛동산 Pick 제철요리 API", description = "맛동산 Pick 제철요리 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dishes")
public class DishController {

    private final DishService dishService;

    @Operation(summary = "제철 요리 투표 이미지 조회", description = "제철 요리 ID로 이미지 조회")
    @GetMapping("/{id}/images")
    public ResponseEntity<CommonResponse<ListResponse<DishImageResponseDto>>> getAllDishImagesById(
            @Parameter(name = "id", description = "조회할 제철 요리 ID", example = "1")
            @PathVariable Long id
    ) {
        return CommonResponse.successList(ResponseCode.OK, dishService.getAllImagesById(id));
    }

    @Operation(summary = "제철 요리 투표 이미지 신고", description = "투표 이미지 ID로 이미지 신고")
    @PostMapping("/images/{id}/report")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> reportVoteImage(
            @Parameter(name = "id", description = "신고할 투표 이미지 ID", example = "1")
            @PathVariable Long id
    ) {
        dishService.reportVoteImage(id);
        return CommonResponse.successResult(ResponseCode.OK, true);
    }

    @Operation(summary = "제철 요리 투표", description = "이미지를 등록하여 제철 요리 투표")
    @PostMapping("/{id}/vote")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> voteDish(
            @Parameter(name = "id", description = "투표할 제철 요리 ID", example = "1")
            @PathVariable Long id
    ) {
        dishService.voteDish(id);
        return CommonResponse.successResult(ResponseCode.OK, true);
    }
}
