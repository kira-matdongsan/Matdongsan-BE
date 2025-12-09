package com.example.matdongsan.auth.application.dto;

import com.example.matdongsan.auth.enums.LoginType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OauthSigninServiceDto {

    private final LoginType loginType;
    private final String token;

}
