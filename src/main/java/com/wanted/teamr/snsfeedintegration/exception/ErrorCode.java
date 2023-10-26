package com.wanted.teamr.snsfeedintegration.exception;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.lang.annotation.Annotation;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATE_ACCOUNT_NAME("이미 같은 이름의 계정이 존재합니다", HttpStatus.BAD_REQUEST),
    BLANK_PROPERTY("입력은 공백일 수 없습니다.", HttpStatus.BAD_REQUEST, NotBlank.class),
    NOT_FOUND_ERROR_CODE("대응하는 에러코드가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
    ;

    private final String message;
    private final HttpStatus httpStatus;
    private final Class<? extends Annotation> code;

    ErrorCode(String message, HttpStatus httpStatus) {
        this(message, httpStatus, null);
    }

    public static ErrorCode getBy(String validationCode) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode() == null) {
                continue;
            }
            if (validationCode.equals(errorCode.getCode().getSimpleName())) {
                return errorCode;
            }
        }
        throw new CustomException(NOT_FOUND_ERROR_CODE);
    }
}
