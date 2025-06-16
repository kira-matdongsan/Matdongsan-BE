package com.example.matdongsan;

import com.example.matdongsan.common.response.CommonResponse;
import com.example.matdongsan.controller.dto.AuthorRequestDto;
import com.example.matdongsan.controller.dto.AuthorResponseDto;
import com.example.matdongsan.controller.dto.BookCreateRequestDto;
import com.example.matdongsan.controller.dto.BookResponseDto;
import com.example.matdongsan.domain.Genre;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void 책_전체_조회_성공() throws Exception {
        AuthorRequestDto authorRequestDto = AuthorRequestDto.builder()
                .name("작가 A")
                .nationality("한국")
                .age(40)
                .build();
        String authorResponse = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        CommonResponse<AuthorResponseDto> authorResponseDto = objectMapper.readValue(
                authorResponse,
                new TypeReference<CommonResponse<AuthorResponseDto>>() {
                }
        );
        Long authorId = authorResponseDto.getData().getId();

        for (int i = 1; i <= 3; i++) {
            BookCreateRequestDto bookRequestDto = BookCreateRequestDto.builder()
                    .authorId(authorId)
                    .title("테스트 책 " + i)
                    .subtitle("부제 " + i)
                    .genre(Genre.FICTION)
                    .isSeries(false)
                    .publishedDate(LocalDate.now())
                    .build();
            mockMvc.perform(post("/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(bookRequestDto)))
                    .andExpect(status().isCreated());
        }

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content").isArray());
    }

    @Test
    void 책_단건_조회_성공() throws Exception {
        AuthorRequestDto authorRequestDto = AuthorRequestDto.builder()
                .name("테스트 작가")
                .nationality("한국")
                .age(40)
                .build();
        String authorResponse = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        CommonResponse<AuthorResponseDto> authorResponseDto = objectMapper.readValue(
                authorResponse,
                new TypeReference<CommonResponse<AuthorResponseDto>>() {
                }
        );
        Long authorId = authorResponseDto.getData().getId();

        BookCreateRequestDto bookRequestDto = BookCreateRequestDto.builder()
                .authorId(authorId)
                .title("조회 테스트 책")
                .subtitle("부제")
                .genre(Genre.NONFICTION)
                .isSeries(false)
                .publishedDate(LocalDate.now())
                .build();
        String bookResponse = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andReturn().getResponse().getContentAsString();
        CommonResponse<BookResponseDto> bookResponseDto = objectMapper.readValue(
                bookResponse,
                new TypeReference<CommonResponse<BookResponseDto>>() {
                }
        );
        Long bookId = bookResponseDto.getData().getId();

        mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(bookId))
                .andExpect(jsonPath("$.data.title").value("조회 테스트 책"));
    }

    @Test
    void 책_등록_성공() throws Exception {
        AuthorRequestDto authorRequestDto = AuthorRequestDto.builder()
                .name("테스트 작가")
                .nationality("한국")
                .age(40)
                .build();
        String authorResponse = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        CommonResponse<AuthorResponseDto> authorResponseDto = objectMapper.readValue(
                authorResponse,
                new TypeReference<CommonResponse<AuthorResponseDto>>() {
                }
        );
        Long authorId = authorResponseDto.getData().getId();

        BookCreateRequestDto bookRequestDto = BookCreateRequestDto.builder()
                .authorId(authorId)
                .title("통합 테스트 책")
                .subtitle("부제")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.title").value("통합 테스트 책"));
    }

    @Test
    void 책_등록_검증_실패() throws Exception {
        BookCreateRequestDto bookRequestDto = BookCreateRequestDto.builder()
                .authorId(1L)
                .title(null)
                .subtitle("부제")
                .genre(Genre.FICTION)
                .isSeries(true)
                .publishedDate(LocalDate.now())
                .build();

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 책_삭제_성공() throws Exception {
        AuthorRequestDto authorRequestDto = AuthorRequestDto.builder()
                .name("테스트 작가")
                .nationality("한국")
                .age(40)
                .build();
        String authorResponse = mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorRequestDto)))
                .andReturn()
                .getResponse()
                .getContentAsString();
        CommonResponse<AuthorResponseDto> authorResponseDto = objectMapper.readValue(
                authorResponse,
                new TypeReference<CommonResponse<AuthorResponseDto>>() {
                }
        );
        Long authorId = authorResponseDto.getData().getId();

        BookCreateRequestDto bookRequestDto = BookCreateRequestDto.builder()
                .authorId(authorId)
                .title("삭제 테스트 책")
                .subtitle("부제")
                .genre(Genre.FICTION)
                .isSeries(false)
                .publishedDate(LocalDate.now())
                .build();
        String bookResponse = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequestDto)))
                .andReturn().getResponse().getContentAsString();
        CommonResponse<BookResponseDto> bookResponseDto = objectMapper.readValue(
                bookResponse,
                new TypeReference<CommonResponse<BookResponseDto>>() {
                }
        );
        Long bookId = bookResponseDto.getData().getId();

        mockMvc.perform(delete("/books/" + bookId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.result").value(true));
    }
}
