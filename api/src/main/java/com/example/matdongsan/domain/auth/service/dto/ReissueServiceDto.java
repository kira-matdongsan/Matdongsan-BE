package com.example.matdongsan.domain.auth.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReissueServiceDto {

    private final String accessToken;
    private final String refreshToken;
}
