package com.example.matdongsan.dish.presentation;

import com.example.matdongsan.response.RestApiResponse;
import com.example.matdongsan.response.ListResponse;
import com.example.matdongsan.response.ResponseCode;
import com.example.matdongsan.response.ResultResponse;
import com.example.matdongsan.dish.application.dto.DishVoteImageServiceDto;
import com.example.matdongsan.dish.presentation.response.DishVoteImageResponse;
import com.example.matdongsan.dish.presentation.request.DishVoteRequest;
import com.example.matdongsan.dish.application.service.DishService;
import com.example.matdongsan.common.util.auth.CurrentUser;
import com.example.matdongsan.user.domain.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "맛동산 Pick 제철요리 API", description = "맛동산 Pick 제철요리 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/dishes")
public class DishController {

    private final DishService dishService;

    @Operation(summary = "제철 요리 투표 이미지 목록 조회", description = "제철 요리 ID로 이미지 조회")
    @GetMapping("/{id}/images")
    public ResponseEntity<RestApiResponse<ListResponse<DishVoteImageResponse>>> getAllDishImagesById(
            @Parameter(name = "id", description = "조회할 제철 요리 ID", example = "1")
            @PathVariable Long id
    ) {
        List<DishVoteImageServiceDto> images = dishService.getAllImagesById(id);
        List<DishVoteImageResponse> responses = images.stream().map(DishVoteImageResponse::from).toList();
        return RestApiResponse.successList(ResponseCode.OK, responses);
    }

    @Operation(summary = "제철 요리 투표 이미지 신고", description = "투표 이미지 ID로 이미지 신고")
    @PostMapping("/images/{id}/report")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> reportVoteImage(
            @Parameter(name = "id", description = "신고할 투표 이미지 ID", example = "1")
            @PathVariable Long id
    ) {
        dishService.reportVoteImage(id);
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }

    @Operation(summary = "제철 요리 투표", description = "이미지를 등록하여 제철 요리 투표")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/vote")
    public ResponseEntity<RestApiResponse<ResultResponse<Boolean>>> voteDish(
            @Parameter(name = "id", description = "투표할 제철 요리 ID", example = "1")
            @PathVariable Long id,
            @CurrentUser User user,
            @RequestBody DishVoteRequest requestDto
    ) {
        dishService.voteDish(id, user.getId(), requestDto.toParam());
        return RestApiResponse.successResult(ResponseCode.OK, true);
    }
}
