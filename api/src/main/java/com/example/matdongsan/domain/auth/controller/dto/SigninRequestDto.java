package com.example.matdongsan.domain.auth.controller.dto;

import com.example.matdongsan.domain.auth.service.dto.SigninServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "이메일 로그인 요청 DTO")
@Builder
@Getter
public class SigninRequestDto {

    @Schema(description = "이메일 주소", example = "test@test.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Schema(description = "비밀번호", example = "password123!!")
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public SigninServiceDto toServiceDto() {
        return SigninServiceDto.builder()
                .email(email)
                .password(password)
                .build();
    }
}
