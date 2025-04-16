package com.example.matdongsan.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Book extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    private String title;
    private String subtitle;
    private Genre genre;
    private Boolean isSeries;
    private LocalDate publishedDate;

    public void putUpdateBook(String title, String subtitle, Genre genre, Boolean isSeries, LocalDate publishedDate) {
        this.title = title;
        this.subtitle = subtitle;
        this.genre = genre;
        this.isSeries = isSeries;
        this.publishedDate = publishedDate;
    }

    public void patchUpdateBook(String title, String subtitle, Genre genre, Boolean isSeries, LocalDate publishedDate) {
        if (title != null) {
            this.title = title;
        }
        if (subtitle != null) {
            this.subtitle = subtitle;
        }
        if (genre != null) {
            this.genre = genre;
        }
        if (isSeries != null) {
            this.isSeries = isSeries;
        }
        if (publishedDate != null) {
            this.publishedDate = publishedDate;
        }
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

}
