package com.wanted.teamr.snsfeedintegration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeType {

    DUPLICATE_ACCOUNT_NAME("이미 같은 이름의 계정이 존재합니다", HttpStatus.BAD_REQUEST),
    PASSWORD_SHORT("비밀번호는 10자 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_REPEATED_CHAR("3회 이상 연속되는 문자를 포함한 비밀번호는 사용할 수 없습니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_SIMPLE("비밀번호는 숫자, 영문, 특수문자 중 2가지 이상을 포함해야 합니다.", HttpStatus.BAD_REQUEST),
    PASSWORD_PERSONAL_INFO("다른 개인정보와 유사한 비밀번호는 사용할 수 없습니다.", HttpStatus.BAD_REQUEST),
//    PASSWORD_MOST_USED("통상적으로 자주 사용되는 비밀번호는 사용할 수 없습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_ERROR_CODE("대응하는 에러코드가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR)
    POST_NOT_FOUND("게시물이 존재하지 않습니다.", HttpStatus.NOT_FOUND),
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
