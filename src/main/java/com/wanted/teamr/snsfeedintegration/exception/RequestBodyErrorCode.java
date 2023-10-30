package com.wanted.teamr.snsfeedintegration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum RequestBodyErrorCode implements ErrorCodeType {

    ACCOUNT_NAME_BLANK("계정 이름은 공백일 수 없습니다.", HttpStatus.BAD_REQUEST, "NotBlank.accountName"),
    EMAIL_BLANK("이메일은 공백일 수 없습니다.", HttpStatus.BAD_REQUEST, "NotBlank.email"),
    PASSWORD_BLANK("비밀번호는 공백일 수 없습니다.", HttpStatus.BAD_REQUEST, "NotBlank.password"),
    APPROVAL_CODE_BLANK("승인코드는 공백일 수 없습니다.", HttpStatus.BAD_REQUEST, "NotBlank.approvalCode"),
    EMAIL_INVALID_FORMAT("이메일 형식이 유효하지 않습니다.", HttpStatus.BAD_REQUEST, "Email.email"),
    APPROVAL_CODE_INVALID_FORMAT("승인코드 형식이 유효하지 않습니다.", HttpStatus.BAD_REQUEST, "Pattern.approvalCode"),
    ;

    private final String message;
    private final HttpStatus httpStatus;
    private final String validationCode;

    public static RequestBodyErrorCode getBy(String validationCode) {
        for (RequestBodyErrorCode errorCode : values()) {
            if (validationCode.equals(errorCode.getValidationCode())) {
                return errorCode;
            }
        }
        throw new CustomException(ErrorCode.NOT_FOUND_ERROR_CODE);
    }

}
