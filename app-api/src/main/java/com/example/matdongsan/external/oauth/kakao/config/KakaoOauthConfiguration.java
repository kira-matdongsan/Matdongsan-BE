package com.example.matdongsan.external.oauth.kakao.config;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KakaoOauthConfiguration {

    @Bean
    public ErrorDecoder kakaoOauthErrorDecoder() {
        return new KakaoOauthErrorDecoder();
    }

    public static class KakaoOauthErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() >= 400 && response.status() <= 499) {
                return new CustomException(ErrorCode.KAKAO_OAUTH_FAILED);
            }
            return new Exception("Feign client error");
        }
    }
}
