package com.wanted.teamr.snsfeedintegration.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ApprovalCodeGeneratorTest {

    @DisplayName("승인코드 생성시 6자리의 숫자,영문 조합")
    @Test
    void test() {
        for (int testCount = 10; testCount > 0; --testCount) {
            String approvalCode = ApprovalCodeGenerator.generate();
            System.out.println("approvalCode = " + approvalCode);
            int len = approvalCode.length();
            assertThat(len).isSameAs(6);
            for (int i = 0; i < len; ++i) {
                char ch = approvalCode.charAt(i);
                assertThat(Character.isDigit(ch) || Character.isAlphabetic(ch)).isTrue();
            }
        }
    }

}