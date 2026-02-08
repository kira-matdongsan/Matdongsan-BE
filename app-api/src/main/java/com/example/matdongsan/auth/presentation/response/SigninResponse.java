package com.example.matdongsan.auth.presentation.response;

import com.example.matdongsan.auth.application.dto.TokenServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "이메일 로그인 응답 DTO")
@Builder
@Getter
public class SigninResponse {

    @Schema(description = "Access Token 값")
    private final String accessToken;

    @Schema(description = "Refresh Token 값")
    private final String refreshToken;

    public static SigninResponse from(TokenServiceDto dto) {
        return SigninResponse.builder()
                .accessToken(dto.getAccessToken())
                .refreshToken(dto.getRefreshToken())
                .build();
    }
}
