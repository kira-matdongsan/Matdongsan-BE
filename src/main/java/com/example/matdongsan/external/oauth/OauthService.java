package com.example.matdongsan.external.oauth;

import com.example.matdongsan.domain.LoginType;
import com.example.matdongsan.external.oauth.dto.OauthResponseDto;
import com.example.matdongsan.external.oauth.kakao.KakaoOauthClient;
import com.example.matdongsan.external.oauth.kakao.dto.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class OauthService {

    @Value("${external.kakao.oauth.client-id}")
    private String kakaoClientId;
    @Value("${external.kakao.oauth.client-secret}")
    private String kakaoClientSecret;

    private final KakaoOauthClient kakaoOauthClient;

    public OauthResponseDto signin(LoginType type, String token) {
        if (type.equals(LoginType.KAKAO)) {
            return kakaoLogin(token);
        } else {
            return OauthResponseDto.builder().build();
        }
    }

    private OauthResponseDto kakaoLogin(String token) {
        String authorization = "Bearer " + token;
        KakaoUserResponse response = kakaoOauthClient.getUserInfo(authorization);

        return OauthResponseDto.builder()
                .oauthId(response.getId().toString())
                .email(response.getKakaoAccount().getEmail())
                .nickname(response.getKakaoAccount().getProfile().getNickname())
                .profileImageUrl(response.getKakaoAccount().getProfile().getProfileImageUrl())
                .build();
    }

}
