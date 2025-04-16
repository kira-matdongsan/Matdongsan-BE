package com.example.matdongsan.controller;

import com.example.matdongsan.common.*;
import com.example.matdongsan.controller.dto.BookCreateRequestDto;
import com.example.matdongsan.controller.dto.BookPatchRequestDto;
import com.example.matdongsan.controller.dto.BookPutRequestDto;
import com.example.matdongsan.controller.dto.BookResponseDto;
import com.example.matdongsan.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "책 API", description = "책 관련 기능을 제공합니다.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "책 목록 조회", description = "페이징 및 정렬 조건으로 책 리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<CommonResponse<PageResponse<BookResponseDto>>> getAllBooks(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return CommonResponse.successPage(ResponseCode.OK, bookService.getAllBooks(pageable));
    }

    @Operation(summary = "책 단건 조회", description = "ID로 특정 책의 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "책을 찾을 수 없음", content = @Content(schema = @Schema(implementation = CommonResponseFail.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<BookResponseDto>> getBookById(
            @Parameter(name= "id", description = "조회할 책 ID", example = "1")
            @PathVariable Long id) {
        return CommonResponse.success(ResponseCode.OK, bookService.getBookById(id));
    }

    @Operation(summary = "책 등록", description = "새로운 책을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "생성 성공", content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "입력값 오류 (Validation)", content = @Content(schema = @Schema(implementation = CommonResponseFail.class))),
            @ApiResponse(responseCode = "404", description = "작가를 찾을 수 없음", content = @Content(schema = @Schema(implementation = CommonResponseFail.class))),
    })
    @PostMapping
    public ResponseEntity<CommonResponse<BookResponseDto>> createBook(@RequestBody @Valid BookCreateRequestDto request) {
        return CommonResponse.success(ResponseCode.CREATED, bookService.createBook(request.toServiceDto()));
    }

    @Operation(summary = "책 수정 (put)", description = "책 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "입력값 오류 (Validation)", content = @Content(schema = @Schema(implementation = CommonResponseFail.class))),
            @ApiResponse(responseCode = "404", description = "책을 찾을 수 없음", content = @Content(schema = @Schema(implementation = CommonResponseFail.class))),
    })
    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<BookResponseDto>> putUpdateBook(
            @Parameter(name= "id", description = "수정할 책 ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid BookPutRequestDto request) {
        return CommonResponse.success(ResponseCode.OK, bookService.putUpdateBook(id, request.toServiceDto()));
    }

    @Operation(summary = "책 수정 (patch)", description = "책 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "입력값 오류 (Validation)", content = @Content(schema = @Schema(implementation = CommonResponseFail.class))),
            @ApiResponse(responseCode = "404", description = "책을 찾을 수 없음", content = @Content(schema = @Schema(implementation = CommonResponseFail.class))),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CommonResponse<BookResponseDto>> patchUpdateBook(
            @Parameter(name= "id", description = "수정할 책 ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid BookPatchRequestDto request) {
        return CommonResponse.success(ResponseCode.OK, bookService.patchUpdateBook(id, request.toServiceDto()));
    }

    @Operation(summary = "책 삭제", description = "책을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content(schema = @Schema(implementation = BookResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "책을 찾을 수 없음", content = @Content(schema = @Schema(implementation = CommonResponseFail.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<ResultResponse<Boolean>>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return CommonResponse.successResult(ResponseCode.OK, true);
    }
}
