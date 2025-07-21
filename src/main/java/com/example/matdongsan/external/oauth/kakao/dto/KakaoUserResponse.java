package com.example.matdongsan.external.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserResponse {

    private Long id;

    private LocalDateTime connectedAt;

    private KakaoAccount kakaoAccount;

}
