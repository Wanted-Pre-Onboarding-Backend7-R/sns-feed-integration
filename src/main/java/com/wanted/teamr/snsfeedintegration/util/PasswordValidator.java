package com.wanted.teamr.snsfeedintegration.util;

import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;

import java.util.StringTokenizer;

public class PasswordValidator {

    // 통상적으로 자주 사용되는 비밀번호는 다른 모든 조건에 스크리닝 되어서 포함하지 않습니다.
    public static void validatePassword(MemberJoinRequest dto) {
        String password = dto.getPassword();
        char[] passwordChars = password.toCharArray();

        int len = passwordChars.length;
        if (len < 10) {
            throw new CustomException(ErrorCode.PASSWORD_SHORT);
        }

        boolean digitExists = false;
        boolean alphabetExists = false;
        boolean specialCharExists = false;
        int countSameChars = 0;
        char prevCh = '\0';
        for (char ch : passwordChars) {
            if (Character.isDigit(ch)) {
                digitExists = true;
            } else if (Character.isAlphabetic(ch)) {
                alphabetExists = true;
            } else {
                specialCharExists = true;
            }
            if (ch == prevCh) {
                if (++countSameChars == 3) {
                    throw new CustomException(ErrorCode.PASSWORD_REPEATED_CHAR);
                }
            } else {
                countSameChars = 1;
            }
            prevCh = ch;
        }
        if ((!digitExists && !alphabetExists)
                || (!alphabetExists && !specialCharExists)
                || (!specialCharExists && !digitExists)) {
            throw new CustomException(ErrorCode.PASSWORD_SIMPLE);
        }

        String emailHead = new StringTokenizer(dto.getEmail(), "@").nextToken();
        if (password.contains(dto.getAccountName()) || password.contains(emailHead)) {
            throw new CustomException(ErrorCode.PASSWORD_PERSONAL_INFO);
        }

    }

}
