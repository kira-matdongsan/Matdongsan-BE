package com.example.matdongsan.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SigninParam {

    private final String email;
    private final String password;
}
