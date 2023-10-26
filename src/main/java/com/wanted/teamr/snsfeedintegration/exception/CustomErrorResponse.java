package com.wanted.teamr.snsfeedintegration.exception;

import lombok.Getter;

@Getter
public class CustomErrorResponse {

    private final String errorCode;
    private final String message;

    public CustomErrorResponse(ErrorCode errorCode) {
        this(errorCode, errorCode.getMessage());
    }

    public CustomErrorResponse(ErrorCode errorCode, String message) {
        this.errorCode = errorCode.name();
        this.message = message;
    }

}
