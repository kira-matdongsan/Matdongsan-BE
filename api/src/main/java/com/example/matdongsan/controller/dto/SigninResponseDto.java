package com.example.matdongsan.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "이메일 로그인 응답 DTO")
@Builder
@Getter
public class SigninResponseDto {

    @Schema(description = "Access Token 값")
    private final String accessToken;

    @Schema(description = "Refresh Token 값")
    private final String refreshToken;
}
