package com.example.matdongsan.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(400, "유효하지 않은 입력입니다.", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED(405, "허용되지 않은 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
    INTERNAL_SERVER_ERROR(500, "서버에 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    // 인증/인가
    UNAUTHORIZED(401, "로그인이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(401, "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN(401, "토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),

    // 파일 처리
    FILE_UPLOAD_FAIL(500, "파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND(404, "파일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 유저/회원
    USER_NOT_FOUND(404, "회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATED_EMAIL(409, "이미 사용 중인 이메일입니다.", HttpStatus.CONFLICT),
    INVALID_PASSWORD(400, "비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST),

    // 도메인 예시 - Book, Author
    BOOK_NOT_FOUND(404, "책을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_BOOK(409, "이미 등록된 책입니다.", HttpStatus.CONFLICT),
    AUTHOR_NOT_FOUND(404, "작가를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    DUPLICATE_AUTHOR(409, "이미 등록된 작가입니다.", HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

}