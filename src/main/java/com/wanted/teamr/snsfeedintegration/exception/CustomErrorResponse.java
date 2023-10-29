package com.wanted.teamr.snsfeedintegration.exception;

import lombok.Getter;

@Getter
public class CustomErrorResponse {

    private final String errorCode;
    private final String message;

    public CustomErrorResponse(ErrorCodeType errorCodeType) {
        errorCode = errorCodeType.name();
        message = errorCodeType.getMessage();
    }

}
