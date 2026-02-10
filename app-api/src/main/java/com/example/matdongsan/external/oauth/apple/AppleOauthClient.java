package com.example.matdongsan.external.oauth.apple;

import com.example.matdongsan.external.oauth.apple.config.AppleOauthConfiguration;
import com.example.matdongsan.external.oauth.apple.dto.ApplePublicKeyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "appleOauthClient", url = "https://appleid.apple.com",
        configuration = AppleOauthConfiguration.class)
public interface AppleOauthClient {

    @GetMapping("/auth/keys")
    ApplePublicKeyResponse getPublicKeys();
}
