package com.example.matdongsan.repository;

import com.example.matdongsan.domain.Author;
import com.example.matdongsan.domain.Book;
import com.example.matdongsan.domain.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    Author author = Author.builder()
            .name("name")
            .nationality("korea")
            .age(20)
            .build();

    @BeforeEach
    void setUp() {
        authorRepository.save(author);
    }

    @Test
    void 책_전체_조회_성공() {
        bookRepository.save(Book.builder()
                .author(authorRepository.findById(author.getId()).orElseThrow())
                .title("책1")
                .subtitle("부제1")
                .genre(Genre.FICTION)
                .isSeries(false)
                .publishedDate(LocalDate.now())
                .build());

        bookRepository.save(Book.builder()
                .author(authorRepository.findById(author.getId()).orElseThrow())
                .title("책2")
                .subtitle("부제2")
                .genre(Genre.NONFICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build());

        assertThat(bookRepository.findAll())
                .hasSize(2)
                .extracting(Book::getTitle)
                .containsExactly("책1", "책2");
    }

    @Test
    void 책_ID로_조회_성공() {
        Book saved = bookRepository.save(Book.builder()
                .author(authorRepository.findById(author.getId()).orElseThrow())
                .title("Java")
                .subtitle("입문")
                .genre(Genre.NONFICTION)
                .isSeries(false)
                .publishedDate(LocalDate.of(2024, 5, 1))
                .build());

        Optional<Book> result = bookRepository.findById(saved.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Java");
    }

    @Test
    void 책_저장_성공() {
        Book book = Book.builder()
                .author(authorRepository.findById(author.getId()).orElseThrow())
                .title("테스트 책")
                .subtitle("테스트 부제")
                .genre(Genre.FICTION)
                .isSeries(false)
                .publishedDate(LocalDate.now())
                .build();

        Book saved = bookRepository.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(bookRepository.findAll()).hasSize(1);
    }

    @Test
    void 책_삭제_성공() {
        Book saved = bookRepository.save(Book.builder()
                .author(authorRepository.findById(author.getId()).orElseThrow())
                .title("삭제될 책")
                .subtitle("테스트")
                .genre(Genre.FICTION)
                .isSeries(false)
                .publishedDate(LocalDate.now())
                .build());

        bookRepository.delete(saved);

        assertThat(bookRepository.findById(saved.getId())).isEmpty();
        assertThat(bookRepository.findAll()).isEmpty();
    }
}
