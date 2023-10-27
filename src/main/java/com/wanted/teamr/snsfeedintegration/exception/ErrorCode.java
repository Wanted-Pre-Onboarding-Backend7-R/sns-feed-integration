package com.wanted.teamr.snsfeedintegration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    DUPLICATE_ACCOUNT_NAME_ERROR("이미 같은 이름의 계정이 존재합니다", HttpStatus.BAD_REQUEST),
    STATISTICS_HASHTAG_NOT_FOUND("존재하지 않는 해시태그입니다.", HttpStatus.NOT_FOUND),
    STATISTICS_PERIOD_MAX_OVER("최대 통계 기간을 초과하였습니다.", HttpStatus.BAD_REQUEST),
    STATISTICS_PERIOD_INVALID("잘못된 통계 기간입니다.", HttpStatus.BAD_REQUEST),
    STATISTICS_STATISTICSVALUE_NOT_FOUND("존재하지 않는 통계값입니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String message;
    private final HttpStatus httpStatus;

}
