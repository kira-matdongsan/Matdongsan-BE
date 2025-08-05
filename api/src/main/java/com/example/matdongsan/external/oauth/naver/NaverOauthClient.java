package com.example.matdongsan.external.oauth.naver;

import com.example.matdongsan.external.oauth.naver.config.NaverOauthConfiguration;
import com.example.matdongsan.external.oauth.naver.dto.NaverUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "naverOauthClient", url = "https://openapi.naver.com", configuration = NaverOauthConfiguration.class)
public interface NaverOauthClient {

    @GetMapping("/v1/nid/me")
    NaverUserResponse getUserInfo(@RequestHeader("Authorization") String token);

}
