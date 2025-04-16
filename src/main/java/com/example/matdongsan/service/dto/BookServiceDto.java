package com.example.matdongsan.service.dto;

import com.example.matdongsan.domain.Author;
import com.example.matdongsan.domain.Book;
import com.example.matdongsan.domain.Genre;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class BookServiceDto {

    private final Long authorId;
    private final String title;
    private final String subtitle;
    private final Genre genre;
    private final Boolean isSeries;
    private final LocalDate publishedDate;

    public Book toBook(Author author) {
        return Book.builder()
                .author(author)
                .title(title)
                .subtitle(subtitle)
                .genre(genre)
                .isSeries(isSeries)
                .publishedDate(publishedDate)
                .build();
    }

}
