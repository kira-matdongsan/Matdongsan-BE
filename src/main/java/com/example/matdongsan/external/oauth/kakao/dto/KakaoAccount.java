package com.example.matdongsan.external.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoAccount {

    private KakaoProfile profile;

    private Boolean hasEmail;

    private String email;
}
