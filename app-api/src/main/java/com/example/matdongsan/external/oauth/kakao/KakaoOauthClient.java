package com.example.matdongsan.external.oauth.kakao;

import com.example.matdongsan.external.oauth.kakao.config.KakaoOauthConfiguration;
import com.example.matdongsan.external.oauth.kakao.dto.KakaoUserResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoOauthClient", url="https://kapi.kakao.com", configuration = KakaoOauthConfiguration.class)
public interface KakaoOauthClient {

    @GetMapping("/v2/user/me")
    @Headers("Content-Type: application/x-www-form-urlencoded;charset=utf-8")
    KakaoUserResponse getUserInfo(@RequestHeader("Authorization") String token);
}
