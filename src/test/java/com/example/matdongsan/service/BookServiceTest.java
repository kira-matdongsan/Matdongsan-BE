package com.example.matdongsan.service;

import com.example.matdongsan.common.exception.CustomException;
import com.example.matdongsan.controller.dto.BookResponseDto;
import com.example.matdongsan.domain.Author;
import com.example.matdongsan.domain.Book;
import com.example.matdongsan.domain.Genre;
import com.example.matdongsan.repository.AuthorRepository;
import com.example.matdongsan.repository.BookRepository;
import com.example.matdongsan.service.dto.BookServiceDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void 모든_책_조회_성공() {
        // given
        Author author = Author.builder()
                .id(1L)
                .name("name")
                .nationality("nationality")
                .age(20)
                .build();
        List<Book> books = List.of(
                Book.builder()
                        .id(1L)
                        .author(author)
                        .title("테스트 제목")
                        .subtitle("테스트 부제목")
                        .genre(Genre.FICTION)
                        .isSeries(true)
                        .publishedDate(LocalDate.now())
                        .build(),
                Book.builder()
                        .id(2L)
                        .author(author)
                        .title("테스트 제목")
                        .subtitle("테스트 부제목")
                        .genre(Genre.FICTION)
                        .isSeries(true)
                        .publishedDate(LocalDate.now())
                        .build()
        );
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), 2L);
        given(bookRepository.findAll(any(Pageable.class))).willReturn(bookPage);

        // when
        Page<BookResponseDto> result = bookService.getAllBooks(Pageable.ofSize(10));

        // then
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void 책_ID로_조회_성공() {
        // given
        Author author = Author.builder()
                .id(1L)
                .name("name")
                .nationality("nationality")
                .age(20)
                .build();
        Book book = Book.builder()
                .id(1L)
                .author(author)
                .title("테스트 제목")
                .subtitle("테스트 부제목")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        // when
        BookResponseDto result = bookService.getBookById(1L);

        // then
        assertNotNull(result);
        assertEquals("테스트 제목", result.getTitle());
    }

    @Test
    void 책_ID로_조회_실패() {
        // given
        given(bookRepository.findById(99L)).willReturn(Optional.empty());

        // when & then
        assertThrows(CustomException.class, () -> bookService.getBookById(99L));
    }

    @Test
    void 책_저장_성공() {
        // given
        BookServiceDto bookServiceDto = BookServiceDto.builder()
                .authorId(1L)
                .title("테스트 제목")
                .subtitle("테스트 부제목")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();

        Author author = Author.builder()
                .id(1L)
                .name("name")
                .nationality("nationality")
                .age(20)
                .build();
        given(authorRepository.findById(bookServiceDto.getAuthorId())).willReturn(Optional.of(author));

        Book book = Book.builder()
                .id(1L)
                .author(author)
                .title("테스트 제목")
                .subtitle("테스트 부제목")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();
        given(bookRepository.save(any(Book.class))).willReturn(book);

        // when
        BookResponseDto result = bookService.createBook(bookServiceDto);

        // then
        assertEquals(1L, result.getId());
        assertEquals("테스트 제목", result.getTitle());
    }

    @Test
    void 책_삭제_성공() {
        // given
        Author author = Author.builder()
                .id(1L)
                .name("name")
                .nationality("nationality")
                .age(20)
                .build();
        Book book = Book.builder()
                .id(1L)
                .author(author)
                .title("테스트 제목")
                .subtitle("테스트 부제목")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();
        given(bookRepository.findById(1L)).willReturn(Optional.of(book));

        // when
        bookService.deleteBook(1L);

        // then
        verify(bookRepository, times(1)).delete(book);
    }
}
