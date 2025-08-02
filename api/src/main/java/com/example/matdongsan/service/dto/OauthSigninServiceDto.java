package com.example.matdongsan.service.dto;

import com.example.matdongsan.jpa.entity.LoginType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OauthSigninServiceDto {

    private final LoginType loginType;
    private final String token;

}
