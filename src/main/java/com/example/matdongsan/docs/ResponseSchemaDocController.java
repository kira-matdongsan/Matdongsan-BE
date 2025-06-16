package com.example.matdongsan.docs;

import com.example.matdongsan.common.response.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "📦 공통 응답 형식", description = "API에서 사용하는 표준 응답 구조를 보여줍니다.")
@RestController
@RequestMapping("/docs/responses")
public class ResponseSchemaDocController {

    @Operation(summary = "공통 성공 응답")
    @GetMapping("/common")
    public CommonResponse<String> common() {
        return CommonResponse.<String>builder()
                .code(200)
                .message("요청 성공")
                .data("예시 데이터")
                .build();
    }

    @Operation(summary = "공통 실패 응답")
    @GetMapping("/fail")
    public CommonResponseFail<String> fail() {
        return CommonResponseFail.<String>builder()
                .code(400)
                .message("잘못된 요청입니다.")
                .errors("필드 오류")
                .build();
    }

    @Operation(summary = "서버 오류 응답")
    @GetMapping("/error")
    public CommonResponseError<String> error() {
        return CommonResponseError.<String>builder()
                .code(500)
                .message("서버 오류")
                .build();
    }

    @Operation(summary = "단일 값 응답")
    @GetMapping("/result")
    public ResultResponse<String> result() {
        return new ResultResponse<>("단일 결과");
    }

    @Operation(summary = "리스트 응답")
    @GetMapping("/list")
    public ListResponse<String> list() {
        return new ListResponse<>(List.of("A", "B", "C"));
    }

    @Operation(summary = "페이지 응답")
    @GetMapping("/page")
    public PageResponse<String> page() {
        return PageResponse.of(
                new PageImpl<>(List.of("A", "B"), PageRequest.of(0, 10), 20)
        );
    }
}
