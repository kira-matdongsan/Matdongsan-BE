package com.example.matdongsan.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReissueParam {

    private final String accessToken;
    private final String refreshToken;
}
