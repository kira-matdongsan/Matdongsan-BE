package com.example.matdongsan.controller.dto;

import com.example.matdongsan.service.dto.SignupServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Schema(description = "이메일 회원가입 요청 DTO")
@Builder
@Getter
public class SignupRequestDto {

    @Schema(description = "이메일 주소", example = "test@test.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Schema(description = "비밀번호", example = "password123!!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    @Schema(description = "동의하는 약관 ID 목록", example = "[1,2,3]")
    private List<Long> termsIds;

    public SignupServiceDto toServiceDto() {
        return SignupServiceDto.builder()
                .email(email)
                .password(password)
                .termsIds(termsIds)
                .build();
    }
}
