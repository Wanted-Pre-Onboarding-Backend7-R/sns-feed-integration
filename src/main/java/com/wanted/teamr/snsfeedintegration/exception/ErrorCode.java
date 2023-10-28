package com.wanted.teamr.snsfeedintegration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATE_ACCOUNT_NAME_ERROR("이미 같은 이름의 계정이 존재합니다", HttpStatus.BAD_REQUEST),
    BAD_TOKEN("잘못된 토큰 정보입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN("만료된 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
    WRONG_TOKEN("지원되지 않는 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
    MALFORMED_TOKEN("잘못된 JWT 서명입니다.", HttpStatus.BAD_REQUEST),
    ILLEGAL_TOKEN("JWT 토큰이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    NOT_SAME_AUTHORITY("권한이 일치하지 않습니다", HttpStatus.BAD_REQUEST),
    REQUIRE_AUTHORITY("필요한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    WRONG_ACCOUNT_INFO("아이디가 없거나 비밀번호가 틀렸습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus httpStatus;

}
