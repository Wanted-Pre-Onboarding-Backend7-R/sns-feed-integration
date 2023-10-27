package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import com.wanted.teamr.snsfeedintegration.util.ApprovalCodeGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService sut;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("사용자 회원가입 서비스")
    @Order(1)
    @Nested
    class Join {

        @DisplayName("계정 이름 중복")
        @Test
        void givenDuplicateAccountName_thenThrowsCustomExceptionByDUPLICATE_ACCOUNT_NAME() {
            // given
            MemberJoinRequest dto = new MemberJoinRequest("jeonggoo75", "jeonggoo75@gmail.com", "qlalfqjsgh486^^");
            String encodedPassword = passwordEncoder.encode(dto.getPassword());
            String approvalCode = ApprovalCodeGenerator.generate();
            Member member = Member.of(dto, encodedPassword, approvalCode);
            memberRepository.save(member);

            // when, then
            Assertions.assertThatThrownBy(() -> sut.join(dto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.DUPLICATE_ACCOUNT_NAME.getMessage());
        }

    }

}
