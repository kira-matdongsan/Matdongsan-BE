package com.example.matdongsan.auth.presentation.request;

import com.example.matdongsan.auth.application.dto.ReissueServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "토큰 재발급 (이메일 로그인) 요청 DTO")
@Builder
@Getter
public class ReissueRequest {

    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;

    public ReissueServiceDto toServiceDto() {
        return ReissueServiceDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
