package com.example.matdongsan.user.application.dto;

import com.example.matdongsan.auth.domain.UserLoginCredential;
import com.example.matdongsan.auth.enums.LoginType;
import com.example.matdongsan.user.domain.UserProfile;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProfileServiceDto {

    private final LoginType loginType;
    private final String email;
    private final String nickname;
    private final String profileImageUrl;

    public static ProfileServiceDto from(UserProfile profile, UserLoginCredential credential) {
        return ProfileServiceDto.builder()
                .loginType(credential.getLoginType())
                .email(credential.getEmail())
                .nickname(profile.getNickname())
                .profileImageUrl(profile.getProfileImageUrl())
                .build();
    }
}
