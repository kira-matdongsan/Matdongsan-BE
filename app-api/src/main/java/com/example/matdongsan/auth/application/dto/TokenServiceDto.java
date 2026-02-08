package com.example.matdongsan.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenServiceDto {

    private final String accessToken;
    private final String refreshToken;

    public static TokenServiceDto of(String accessToken, String refreshToken) {
        return TokenServiceDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
