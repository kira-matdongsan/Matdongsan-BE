package com.example.matdongsan.domain.auth.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SigninServiceDto {

    private final String email;
    private final String password;
}
