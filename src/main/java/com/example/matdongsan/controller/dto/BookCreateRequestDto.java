package com.example.matdongsan.controller.dto;

import com.example.matdongsan.domain.Genre;
import com.example.matdongsan.service.dto.BookServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Schema(description = "책 등록 요청 DTO")
@Builder
@Getter
public class BookCreateRequestDto {

    @Schema(description = "저자 ID", example = "1")
    @NotNull
    private Long authorId;

    @Schema(description = "책 제목", example = "Clean Code")
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 50, message = "제목은 50자 이내여야 합니다.")
    private String title;

    @Schema(description = "부제목", example = "Java 최고의 클린 코드 가이드")
    @Size(max = 100, message = "부제목은 100자 이내여야 합니다.")
    private String subtitle;

    @Schema(description = "장르", example = "FICTION")
    @NotNull
    private Genre genre;

    @Schema(description = "시리즈 여부", example = "false")
    @NotNull
    private Boolean isSeries;

    @Schema(description = "출간일", example = "2024-01-15")
    @NotNull
    private LocalDate publishedDate;

    public BookServiceDto toServiceDto() {
        return BookServiceDto.builder()
                .authorId(authorId)
                .title(title)
                .subtitle(subtitle)
                .genre(genre)
                .isSeries(isSeries)
                .publishedDate(publishedDate)
                .build();
    }

}
