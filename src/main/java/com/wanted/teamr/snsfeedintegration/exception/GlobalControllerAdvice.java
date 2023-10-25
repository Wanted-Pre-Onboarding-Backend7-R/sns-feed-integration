package com.wanted.teamr.snsfeedintegration.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler
    public CustomErrorResponse handler(CustomException exception, HttpServletResponse response) {
        log.error("MyException ", exception);
        response.setStatus(HttpStatus.OK.value());
        return new CustomErrorResponse(exception.getErrorCode());
    }

}
