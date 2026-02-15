package com.example.matdongsan.user.presentation.response;

import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.user.application.dto.ProfileServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "프로필 응답 DTO")
@Builder
@Getter
public class ProfileResponse {

    @Schema(description = "로그인 타입", example = "KAKAO")
    private final LoginType loginType;

    @Schema(description = "이메일", example = "user@example.com")
    private final String email;

    @Schema(description = "닉네임", example = "행복한사자")
    private final String nickname;

    @Schema(description = "프로필 이미지 URL")
    private final String profileImageUrl;

    public static ProfileResponse from(ProfileServiceDto dto) {
        return ProfileResponse.builder()
                .loginType(dto.getLoginType())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .profileImageUrl(dto.getProfileImageUrl())
                .build();
    }
}
