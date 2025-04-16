package com.example.matdongsan.controller.dto;

import com.example.matdongsan.domain.Book;
import com.example.matdongsan.domain.Genre;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Schema(description = "책 응답 DTO")
@Builder
@Getter
public class BookResponseDto {

    @Schema(description = "책 ID", example = "1")
    private final Long id;

    @Schema(description = "저자 정보")
    private final AuthorResponseDto author;

    @Schema(description = "책 제목", example = "Clean Code")
    private final String title;

    @Schema(description = "부제목", example = "자바로 배우는 클린코드")
    private final String subtitle;

    @Schema(description = "장르", example = "FICTION")
    private final Genre genre;

    @Schema(description = "시리즈 여부", example = "false")
    private final Boolean isSeries;

    @Schema(description = "출간일", example = "2024-01-01")
    private final LocalDate publishedDate;

    @Schema(description = "생성일시", example = "2024-01-01 10:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    @Schema(description = "수정일시", example = "2024-01-02 15:30:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime updatedAt;

    public static BookResponseDto of(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .author(AuthorResponseDto.of(book.getAuthor()))
                .title(book.getTitle())
                .subtitle(book.getSubtitle())
                .genre(book.getGenre())
                .isSeries(book.getIsSeries())
                .publishedDate(book.getPublishedDate())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
