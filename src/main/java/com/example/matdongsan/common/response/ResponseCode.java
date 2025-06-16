package com.example.matdongsan.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK(200, "요청이 성공했습니다.", HttpStatus.OK),
    CREATED(201, "자원이 생성되었습니다.", HttpStatus.CREATED),
    ACCEPTED(202, "요청이 접수되었습니다.", HttpStatus.ACCEPTED),
    NO_CONTENT(204, "콘텐츠 없음", HttpStatus.NO_CONTENT);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;

}
