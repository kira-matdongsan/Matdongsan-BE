package com.example.matdongsan.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "제철음식 이야기 목록 응답 DTO")
@Builder
@Getter
public class StoryResponseDto {

    @Schema(description = "이야기 ID", example = "1")
    private final Long id;

    @Schema(description = "닉네임", example = "도란도란")
    private final String nickname;

    @Schema(description = "프로필 이미지 URL", example = "")
    private final String profileImageUrl;

    @Schema(description = "이야기 타입", example = "SEASONAL_NOTE")
    private final String type;

    @Schema(description = "글(제철 기록), 한줄평(플레이스)", example = "길가에 트럭을 보면 그냥 지나치지 못하고 항상 옥수수를 사먹는데 이태원 길가에 있던 옥수수 사장님이 오늘 개시 손님이라고 해서 뭔가 기분이 좋았당! 집에 가서 먹으려고 했는데 못참고 길옥수수를 했다.")
    private final String content;

    @Schema(description = "좋아요 수", example = "3")
    private final Integer likeCount;

    @Schema(description = "좋아요 여부", example = "TRUE")
    private final Boolean isLiked;

    @Schema(description = "이미지 목록")
    private final List<StoryImageResponseDto> images;

    @Schema(description = "작성일시", example = "2025-05-07 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    // 제철 기록
    @Schema(description = "기록일", example = "2025-05-06")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private final LocalDate recordedDate;

    // 레시피 + 플레이스
    @Schema(description = "레시피명(레시피), 가게 이름(플레이스)", example = "토오베")
    private final String name;

    // 레시피
    @Schema(description = "재료", example = "옥수수, 소금 2T, 뉴슈가 1T")
    private final String ingredients;
    @Schema(description = "조리방법", example = "옥수수 껍질을 두 세장 남겨두고 나머지는 다 손질해줍니다.")
    private final String instructions;

    // 플레이스
    @Schema(description = "가게 카테고리", example = "차 전문점")
    private final String category;
    @Schema(description = "가게 주소", example = "서울특별시 종로구 관훈동 118-36 3층")
    private final String address;
    @Schema(description = "가게 네이버 지도 URL", example = "")
    private final String naverUrl;

}
