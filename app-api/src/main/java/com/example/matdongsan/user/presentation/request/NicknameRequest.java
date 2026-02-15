package com.example.matdongsan.user.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "닉네임 변경 요청 DTO")
@Getter
public class NicknameRequest {

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Schema(description = "변경할 닉네임", example = "행복한사자")
    private String nickname;
}
