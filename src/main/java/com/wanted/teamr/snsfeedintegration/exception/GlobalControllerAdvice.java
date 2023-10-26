package com.wanted.teamr.snsfeedintegration.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalControllerAdvice {

    private final MessageSource messageSource;

    @ExceptionHandler
    public CustomErrorResponse handler(CustomException exception, HttpServletResponse response) {
        log.error("MyException ", exception);
        response.setStatus(HttpStatus.OK.value());
        return new CustomErrorResponse(exception.getErrorCode());
    }

    @ExceptionHandler
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);

        FieldError firstFieldError = e.getBindingResult().getFieldErrors().get(0);
        String validationCode = Objects.requireNonNull(firstFieldError.getCode());
        String message = messageSource.getMessage(
                validationCode,
                new String[]{firstFieldError.getField()},
                Locale.getDefault());
        log.error("firstFieldError.getField(): {}", firstFieldError.getField());
        log.error("validationCode: {}", validationCode);
        log.error("message: {}", message);

        ErrorCode errorCode = ErrorCode.getBy(validationCode);
        CustomErrorResponse body = new CustomErrorResponse(errorCode, message);
        return ResponseEntity.status(errorCode.getHttpStatus())
                .header(HttpHeaders.CONTENT_ENCODING, Encoding.DEFAULT_CHARSET.name())
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
    }

}
