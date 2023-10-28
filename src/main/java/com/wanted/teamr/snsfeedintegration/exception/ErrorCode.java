package com.wanted.teamr.snsfeedintegration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATE_ACCOUNT_NAME_ERROR("이미 같은 이름의 계정이 존재합니다", HttpStatus.BAD_REQUEST),
    POST_NOT_FOUND("게시물이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
