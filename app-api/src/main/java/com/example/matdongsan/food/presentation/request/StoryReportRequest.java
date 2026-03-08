package com.example.matdongsan.food.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "이야기 신고 요청 DTO")
@Builder
@Getter
public class StoryReportRequest {

    @NotBlank(message = "신고 사유를 입력해주세요.")
    @Size(max = 255, message = "신고 사유는 255자 이하여야 합니다.")
    private String reason;
}
