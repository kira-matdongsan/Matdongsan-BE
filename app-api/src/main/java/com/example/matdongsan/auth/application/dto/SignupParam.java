package com.example.matdongsan.auth.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SignupParam {

    private final String email;
    private final String password;
    private final List<Long> termsIds;

}
