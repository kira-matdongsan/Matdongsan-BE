package com.example.matdongsan.controller.dto;

import com.example.matdongsan.service.dto.SignupServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "이메일 회원가입 요청 DTO")
@Builder
@Getter
public class SignupRequestDto {

    private String email;
    private String password;
    private List<Long> termsIds;

    public SignupServiceDto toServiceDto() {
        return SignupServiceDto.builder()
                .email(email)
                .password(password)
                .termsIds(termsIds)
                .build();
    }
}
