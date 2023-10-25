package com.wanted.teamr.skeleton.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATE_ACCOUNT_NAME_ERROR("이미 같은 이름의 계정이 존재합니다", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
