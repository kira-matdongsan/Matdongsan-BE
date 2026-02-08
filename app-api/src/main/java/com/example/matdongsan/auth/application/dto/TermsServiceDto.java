package com.example.matdongsan.auth.application.dto;

import com.example.matdongsan.auth.domain.Terms;
import com.example.matdongsan.auth.enums.TermsType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TermsServiceDto {

    private final Long id;
    private final TermsType type;
    private final String title;
    private final String content;
    private final Boolean required;

    public static TermsServiceDto from(Terms terms) {
        return TermsServiceDto.builder()
                .id(terms.getId())
                .type(terms.getType())
                .title(terms.getTitle())
                .content(terms.getContent())
                .required(terms.getRequired())
                .build();
    }
}
