package com.example.matdongsan.external.oauth.naver.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverUserProfile {

    private String id;
    private String email;
    private String nickname;
    private String profileImage;

}
