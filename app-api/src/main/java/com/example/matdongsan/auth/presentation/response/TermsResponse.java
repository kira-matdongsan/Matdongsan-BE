package com.example.matdongsan.auth.presentation.response;

import com.example.matdongsan.jpa.entity.auth.Terms;
import com.example.matdongsan.auth.enums.TermsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "약관 목록 조회 응답 DTO")
@Builder
@Getter
public class TermsResponse {

    @Schema(description = "약관 ID", example = "1")
    private final Long id;

    @Schema(description = "약관 타입", example = "SERVICE")
    private final TermsType type;

    @Schema(description = "약관 제목", example = "이용약관")
    private final String title;

    @Schema(description = "약관 내용", example = "어쩌구저쩌구")
    private final String content;

    @Schema(description = "약관 필수 여부", example = "true")
    private final Boolean required;

    public static TermsResponse of(Terms terms) {
        return TermsResponse.builder()
                .id(terms.getId())
                .type(terms.getType())
                .title(terms.getTitle())
                .content(terms.getContent())
                .required(terms.getRequired())
                .build();
    }
}
