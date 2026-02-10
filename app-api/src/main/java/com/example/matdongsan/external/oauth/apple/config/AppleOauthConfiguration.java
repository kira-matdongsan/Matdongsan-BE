package com.example.matdongsan.external.oauth.apple.config;

import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppleOauthConfiguration {

    @Bean
    public ErrorDecoder appleOauthErrorDecoder() {
        return new AppleOauthErrorDecoder();
    }

    public static class AppleOauthErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.status() >= 400 && response.status() <= 499) {
                return new CustomException(ErrorCode.APPLE_OAUTH_FAILED);
            }
            return new Exception("Feign client error");
        }
    }
}
