package com.wanted.teamr.snsfeedintegration.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorCodeType errorCodeType;

    public CustomException(ErrorCodeType errorCodeType) {
        super(errorCodeType.getMessage());
        this.errorCodeType = errorCodeType;
    }

    public CustomException(ErrorCodeType errorCodeType, Throwable cause) {
        super(errorCodeType.getMessage(), cause);
        this.errorCodeType = errorCodeType;
    }

}
