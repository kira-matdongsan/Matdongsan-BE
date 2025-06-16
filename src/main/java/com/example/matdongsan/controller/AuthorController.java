package com.example.matdongsan.controller;

import com.example.matdongsan.common.response.CommonResponse;
import com.example.matdongsan.common.response.ResponseCode;
import com.example.matdongsan.common.response.ListResponse;
import com.example.matdongsan.common.response.ResultResponse;
import com.example.matdongsan.controller.dto.AuthorRequestDto;
import com.example.matdongsan.controller.dto.AuthorResponseDto;
import com.example.matdongsan.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sample/authors")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<CommonResponse<ListResponse<AuthorResponseDto>>> getAllAuthors() {
        return CommonResponse.successList(ResponseCode.OK, authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<AuthorResponseDto>> getAuthorById(@PathVariable Long id) {
        return CommonResponse.success(ResponseCode.OK, authorService.getAuthorById(id));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<AuthorResponseDto>> createAuthor(@RequestBody @Valid AuthorRequestDto request) {
        return CommonResponse.success(ResponseCode.CREATED, authorService.createAuthor(request.toServiceDto()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return CommonResponse.successResult(ResponseCode.OK, true);
    }
}
