package com.example.matdongsan.external.oauth.naver.dto;

import lombok.Data;

@Data
public class NaverUserResponse {

    private String resultcode;
    private String message;
    private NaverUserProfile response;

}
