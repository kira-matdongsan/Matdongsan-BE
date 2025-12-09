package com.example.matdongsan.external.oauth.naver.config;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class NaverOauthConfiguration {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new NaverOauthErrorDecoder();
    }

    private static class NaverOauthErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() >= 400 && response.status() <= 499) {
                return new CustomException(ErrorCode.NAVER_OAUTH_FAILED);
            }
            return new Exception("Feign client error");
        }
    }
}
