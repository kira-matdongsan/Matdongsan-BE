package com.example.matdongsan.food.presentation.response;

import com.example.matdongsan.food.application.dto.DishPickServiceDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "맛동산 Pick 제철요리 응답 DTO")
@Builder
@Getter
public class DishPickResponse {

    @Schema(description = "총 투표 수", example = "54")
    private final Integer totalVoteCount;

    @Schema(description = "투표 시작일", example = "2025-07-07")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate voteStartDate;

    @Schema(description = "투표 마감일", example = "2025-07-13")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate voteEndDate;

    @Schema(description = "제철요리 목록 (순위 정렬)")
    private final List<DishResponse> contents;

    public static DishPickResponse from(DishPickServiceDto dto) {
        List<DishResponse> contents = dto.getContents().stream()
                .map(DishResponse::from)
                .toList();

        return DishPickResponse.builder()
                .totalVoteCount(dto.getTotalVoteCount())
                .voteStartDate(dto.getVoteStartDate())
                .voteEndDate(dto.getVoteEndDate())
                .contents(contents)
                .build();
    }
}
