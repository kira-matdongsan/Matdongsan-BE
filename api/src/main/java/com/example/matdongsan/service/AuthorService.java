package com.example.matdongsan.service;


import com.example.matdongsan.controller.dto.AuthorResponseDto;
import com.example.matdongsan.exception.CustomException;
import com.example.matdongsan.exception.ErrorCode;
import com.example.matdongsan.jpa.entity.Author;
import com.example.matdongsan.jpa.repository.AuthorRepository;
import com.example.matdongsan.service.dto.AuthorServiceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<AuthorResponseDto> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream().map(AuthorResponseDto::of).toList();
    }

    public AuthorResponseDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.AUTHOR_NOT_FOUND));
        return AuthorResponseDto.of(author);
    }

    @Transactional
    public AuthorResponseDto createAuthor(AuthorServiceDto serviceDto) {
        Author savedAuthor = authorRepository.save(serviceDto.toAuthor());
        return AuthorResponseDto.of(savedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.AUTHOR_NOT_FOUND));
        authorRepository.delete(author);
    }
}
