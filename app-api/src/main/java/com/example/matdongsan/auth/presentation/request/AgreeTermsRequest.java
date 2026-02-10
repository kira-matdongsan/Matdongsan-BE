package com.example.matdongsan.auth.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Schema(description = "약관 동의 요청 DTO")
@Getter
public class AgreeTermsRequest {

    @Schema(description = "동의하는 약관 ID 목록", example = "[1, 2, 3]")
    @NotEmpty(message = "약관 ID 목록을 입력해주세요.")
    private List<Long> termsIds;
}
