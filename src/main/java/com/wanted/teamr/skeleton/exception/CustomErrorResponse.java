package com.wanted.teamr.skeleton.exception;

import lombok.Getter;

@Getter
public class CustomErrorResponse {

    private final String errorCode;
    private final String message;

    public CustomErrorResponse(ErrorCode errorCode) {
        this.errorCode = errorCode.name();
        this.message = errorCode.getMessage();
    }

}
