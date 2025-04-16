package com.example.matdongsan.controller;

import com.example.matdongsan.controller.dto.AuthorResponseDto;
import com.example.matdongsan.controller.dto.BookCreateRequestDto;
import com.example.matdongsan.controller.dto.BookPutRequestDto;
import com.example.matdongsan.controller.dto.BookResponseDto;
import com.example.matdongsan.domain.Genre;
import com.example.matdongsan.service.BookService;
import com.example.matdongsan.service.dto.BookServiceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    void 책_전체_조회_성공() throws Exception {
        // given
        AuthorResponseDto authorResponseDto = AuthorResponseDto.builder()
                .id(1L)
                .name("작가")
                .nationality("한국")
                .age(25)
                .build();
        List<BookResponseDto> bookResponseDtoList = List.of(
                BookResponseDto.builder()
                        .id(1L)
                        .author(authorResponseDto)
                        .title("제목1")
                        .subtitle("부제1")
                        .genre(Genre.FICTION)
                        .isSeries(true)
                        .publishedDate(LocalDate.now())
                        .build(),
                BookResponseDto.builder()
                        .id(2L)
                        .author(authorResponseDto)
                        .title("제목2")
                        .subtitle("부제2")
                        .genre(Genre.FICTION)
                        .isSeries(true)
                        .publishedDate(LocalDate.now())
                        .build()
        );
        Page<BookResponseDto> bookResponseDtoPage = new PageImpl<>(bookResponseDtoList, PageRequest.of(0, 10), 2L);
        given(bookService.getAllBooks(any(Pageable.class))).willReturn(bookResponseDtoPage);

        // when & then
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void 책_단건_조회_성공() throws Exception {
        // given
        AuthorResponseDto authorResponseDto = AuthorResponseDto.builder()
                .id(1L)
                .name("작가")
                .nationality("한국")
                .age(25)
                .build();
        BookResponseDto bookResponseDto = BookResponseDto.builder()
                .id(1L)
                .author(authorResponseDto)
                .title("제목")
                .subtitle("부제")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();

        given(bookService.getBookById(1L)).willReturn(bookResponseDto);

        // when & then
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("제목"));
    }

    @Test
    void 책_등록_성공() throws Exception {
        // given
        BookCreateRequestDto requestDto =  BookCreateRequestDto.builder()
                .authorId(1L)
                .title("제목")
                .subtitle("부제")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();
        AuthorResponseDto authorResponseDto = AuthorResponseDto.builder()
                .id(1L)
                .name("작가")
                .nationality("한국")
                .age(25)
                .build();
        BookResponseDto bookResponseDto = BookResponseDto.builder()
                .id(1L)
                .author(authorResponseDto)
                .title("제목")
                .subtitle("부제")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();

        given(bookService.createBook(any(BookServiceDto.class))).willReturn(bookResponseDto);

        // when & then
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("자원이 생성되었습니다."))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("제목"));
    }


    @Test
    void 책_등록_실패() throws Exception {
        // given: 필수 필드 누락 (예: title이 null)
        BookCreateRequestDto invalidDto = BookCreateRequestDto.builder()
                .authorId(1L)
                .title(null) // title 누락
                .subtitle("부제")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();

        // when & then
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.errors").exists());
    }




    @Test
    void 책_수정_성공() throws Exception {
        // given
        BookPutRequestDto requestDto = BookPutRequestDto.builder()
                .title("수정된 제목")
                .subtitle("수정된 부제")
                .genre(Genre.FICTION)
                .isSeries(false)
                .publishedDate(LocalDate.now())
                .build();
        AuthorResponseDto authorResponseDto = AuthorResponseDto.builder()
                .id(1L)
                .name("작가")
                .nationality("한국")
                .age(25)
                .build();
        BookResponseDto bookResponseDto = BookResponseDto.builder()
                .id(1L)
                .author(authorResponseDto)
                .title("수정된 제목")
                .subtitle("수정된 부제")
                .genre(Genre.FICTION)
                .isSeries(false)
                .publishedDate(LocalDate.now())
                .build();

        given(bookService.putUpdateBook(eq(1L), any(BookServiceDto.class))).willReturn(bookResponseDto);

        // when & then
        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("수정된 제목"));
    }

    @Test
    void 책_삭제_성공() throws Exception {
        // when & then
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.result").value(true));
    }
}
