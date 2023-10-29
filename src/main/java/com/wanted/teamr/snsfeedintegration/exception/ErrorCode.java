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

    POST_NOT_FOUND("게시물이 존재하지 않습니다.", HttpStatus.NOT_FOUND),

    NOT_FOUND_ERROR_CODE("대응하는 에러코드가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    STATISTICS_HASHTAG_NOT_FOUND("존재하지 않는 해시태그입니다.", HttpStatus.NOT_FOUND),
    STATISTICS_PERIOD_MAX_OVER("최대 통계 기간을 초과하였습니다.", HttpStatus.BAD_REQUEST),
    STATISTICS_PERIOD_INVALID("잘못된 통계 기간입니다.", HttpStatus.BAD_REQUEST),
    STATISTICS_STATISTICSVALUE_NOT_FOUND("존재하지 않는 통계값입니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
