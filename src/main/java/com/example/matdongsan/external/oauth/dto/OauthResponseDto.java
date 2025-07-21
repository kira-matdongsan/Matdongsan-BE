package com.example.matdongsan.external.oauth.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OauthResponseDto {

    private final String oauthId;
    private final String email;
    private final String nickname;
    private final String profileImageUrl;
    private final List<Long> termsIds;

}
