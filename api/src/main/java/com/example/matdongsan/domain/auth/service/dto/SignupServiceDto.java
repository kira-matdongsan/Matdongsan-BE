package com.example.matdongsan.domain.auth.service.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SignupServiceDto {

    private final String email;
    private final String password;
    private final List<Long> termsIds;

}
