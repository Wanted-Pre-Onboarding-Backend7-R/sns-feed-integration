package com.wanted.teamr.snsfeedintegration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeType {

    PASSWORD_SHORT("비밀번호는 10자 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_REPEATED_CHAR("3회 이상 연속되는 문자를 포함한 비밀번호는 사용할 수 없습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_SIMPLE("비밀번호는 숫자, 영문, 특수문자 중 2가지 이상을 포함해야 합니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_PERSONAL_INFO("다른 개인정보와 유사한 비밀번호는 사용할 수 없습니다.", HttpStatus.BAD_REQUEST),
//    PASSWORD_MOST_USED("통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다.", HttpStatus.BAD_REQUEST),
    DUPLICATE_ACCOUNT_NAME("이미 같은 이름의 계정이 존재합니다", HttpStatus.BAD_REQUEST),
    ACCOUNT_INFO_WRONG("계정 이름 또는 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST),
    APPROVAL_CODE_WRONG("승인코드가 틀렸습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_APPROVED("이미 가입승인이 완료되었습니다", HttpStatus.BAD_REQUEST),

    BAD_TOKEN("잘못된 토큰 정보입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN("만료된 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
    WRONG_TOKEN("지원되지 않는 JWT 토큰입니다.", HttpStatus.BAD_REQUEST),
    MALFORMED_TOKEN("잘못된 JWT 서명입니다.", HttpStatus.BAD_REQUEST),
    ILLEGAL_TOKEN("JWT 토큰이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    NOT_SAME_AUTHORITY("권한이 일치하지 않습니다", HttpStatus.BAD_REQUEST),
    REQUIRE_AUTHORITY("필요한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    REQUIRE_APPROVAL("가입승인이 필요합니다.", HttpStatus.BAD_REQUEST),

    POST_NOT_FOUND("게시물이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    NOT_FOUND_ERROR_CODE("대응하는 에러코드가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
