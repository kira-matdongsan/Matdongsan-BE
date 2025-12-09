package com.example.matdongsan.exception;

import com.example.matdongsan.response.RestApiResponseError;
import com.example.matdongsan.response.RestApiResponseFail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
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
    public ResponseEntity<RestApiResponseFail<Void>> handleCustomException(CustomException ex) {
        log.warn("[handleCustomException] ErrorCode::{} - {}", ex.getErrorCode(), ex.getErrorCode().getMessage(), ex);
        return RestApiResponseFail.fail(ex.getErrorCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestApiResponseFail<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("[handleValidationException] {}", ex.getClass().getName(), ex);
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage,
                        (existing, replacement) -> existing
                ));

        return RestApiResponseFail.fail(ErrorCode.INVALID_INPUT_VALUE, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestApiResponseFail<Void>> handleEnumTypeMismatch(HttpMessageNotReadableException ex) {
        log.warn("[handleEnumTypeMismatch] {}", ex.getClass().getName(), ex);
        return RestApiResponseFail.fail(ErrorCode.INVALID_INPUT_VALUE);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestApiResponseFail<Void>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("[handleAccessDeniedException] {}", ex.getClass().getName(), ex);
        return RestApiResponseFail.fail(ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestApiResponseError<Void>> handleException(Exception ex) {
        log.error("[handleException] {}", ex.getClass().getName(), ex);
        return RestApiResponseError.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

}
