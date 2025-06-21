package com.example.matdongsan.common.exception;

import com.example.matdongsan.common.response.CommonResponseError;
import com.example.matdongsan.common.response.CommonResponseFail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponseFail<Void>> handleCustomException(CustomException ex) {
        log.warn("[handleCustomException] ErrorCode::{} - {}", ex.getErrorCode(), ex.getErrorCode().getMessage(), ex);
        return CommonResponseFail.fail(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseFail<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("[handleValidationException] {}", ex.getClass().getName(), ex);
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        return CommonResponseFail.fail(ErrorCode.INVALID_INPUT_VALUE, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponseFail<Void>> handleEnumTypeMismatch(HttpMessageNotReadableException ex) {
        log.warn("[handleEnumTypeMismatch] {}", ex.getClass().getName(), ex);
        return CommonResponseFail.fail(ErrorCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseError<Void>> handleException(Exception ex) {
        log.error("[handleException] {}", ex.getClass().getName(), ex);
        return CommonResponseError.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
