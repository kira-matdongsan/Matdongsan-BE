package com.example.matdongsan.auth.presentation.request;

import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.auth.application.dto.OauthSigninServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "Oauth2 (카카오/네이버) 로그인 요청 DTO")
@Builder
@Getter
public class OauthSigninRequest {

    @Schema(description = "OAuth2 제공자", example = "KAKAO")
    @NotNull(message = "provider를 입력해주세요.")
    private LoginType provider;

    @Schema(description = "OAuth2 서비스 토큰", example = "token")
    @NotBlank(message = "token 값을 입력해주세요.")
    private String token;

    public OauthSigninServiceDto toServiceDto() {
        return OauthSigninServiceDto.builder()
                .loginType(provider)
                .token(token)
                .build();
    }
}
