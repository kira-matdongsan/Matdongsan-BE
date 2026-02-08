package com.example.matdongsan.auth.application.dto;

import com.example.matdongsan.auth.enums.LoginType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OauthSigninParam {

    private final LoginType loginType;
    private final String token;

}
