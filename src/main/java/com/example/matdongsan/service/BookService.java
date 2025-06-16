package com.example.matdongsan.service;

import com.example.matdongsan.common.exception.ErrorCode;
import com.example.matdongsan.common.exception.CustomException;
import com.example.matdongsan.controller.dto.BookResponseDto;
import com.example.matdongsan.domain.Author;
import com.example.matdongsan.domain.Book;
import com.example.matdongsan.repository.AuthorRepository;
import com.example.matdongsan.repository.BookRepository;
import com.example.matdongsan.service.dto.BookServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Page<BookResponseDto> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable).map(BookResponseDto::of);
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
        return BookResponseDto.of(book);
    }

    @Transactional
    public BookResponseDto createBook(BookServiceDto serviceDto) {
        Author author = authorRepository.findById(serviceDto.getAuthorId()).orElseThrow(() -> new CustomException(ErrorCode.AUTHOR_NOT_FOUND));
        Book savedBook = bookRepository.save(serviceDto.toBook(author));
        return BookResponseDto.of(savedBook);
    }

    @Transactional
    public BookResponseDto putUpdateBook(Long id, BookServiceDto serviceDto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
        book.putUpdateBook(serviceDto.getTitle(), serviceDto.getSubtitle(), serviceDto.getGenre(), serviceDto.getIsSeries(), serviceDto.getPublishedDate());
        return BookResponseDto.of(book);
    }

    @Transactional
    public BookResponseDto patchUpdateBook(Long id, BookServiceDto serviceDto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
        book.patchUpdateBook(serviceDto.getTitle(), serviceDto.getSubtitle(), serviceDto.getGenre(), serviceDto.getIsSeries(), serviceDto.getPublishedDate());
        return BookResponseDto.of(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.BOOK_NOT_FOUND));
        bookRepository.delete(book);
    }
}
