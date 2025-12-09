package com.example.matdongsan.external.oauth;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.external.oauth.dto.OauthResponseDto;
import com.example.matdongsan.external.oauth.kakao.KakaoOauthClient;
import com.example.matdongsan.external.oauth.kakao.dto.KakaoAccount;
import com.example.matdongsan.external.oauth.kakao.dto.KakaoProfile;
import com.example.matdongsan.external.oauth.kakao.dto.KakaoUserResponse;
import com.example.matdongsan.external.oauth.naver.NaverOauthClient;
import com.example.matdongsan.external.oauth.naver.dto.NaverUserProfile;
import com.example.matdongsan.external.oauth.naver.dto.NaverUserResponse;
import com.example.matdongsan.auth.enums.LoginType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OauthService {

    private final KakaoOauthClient kakaoOauthClient;
    private final NaverOauthClient naverOauthClient;

    public OauthResponseDto signin(LoginType type, String token) {
        if (type.equals(LoginType.KAKAO)) {
            return kakaoLogin(token);
        } else if (type.equals(LoginType.NAVER)) {
            return naverLogin(token);
        } else {
            throw new CustomException(ErrorCode.BAD_REQUEST, "올바른 Provider 값을 입력하세요.");
        }
    }

    private OauthResponseDto kakaoLogin(String token) {
        String authorization = "Bearer " + token;
        KakaoUserResponse response = kakaoOauthClient.getUserInfo(authorization);

        String nickname = Optional.ofNullable(response.getKakaoAccount())
                .map(KakaoAccount::getProfile)
                .map(KakaoProfile::getNickname)
                .orElse(null);

        String profileImageUrl = Optional.ofNullable(response.getKakaoAccount())
                .map(KakaoAccount::getProfile)
                .map(KakaoProfile::getProfileImageUrl)
                .orElse(null);

        return OauthResponseDto.builder()
                .oauthId(response.getId().toString())
                .email(response.getKakaoAccount().getEmail())
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }

    private OauthResponseDto naverLogin(String token) {
        String authorization = "Bearer " + token;
        NaverUserResponse response = naverOauthClient.getUserInfo(authorization);

        String nickname = Optional.ofNullable(response.getResponse())
                .map(NaverUserProfile::getNickname)
                .orElse(null);

        String profileImageUrl = Optional.ofNullable(response.getResponse())
                .map(NaverUserProfile::getProfileImage)
                .orElse(null);

        return OauthResponseDto.builder()
                .oauthId(response.getResponse().getId())
                .email(response.getResponse().getEmail())
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .build();
    }

}
