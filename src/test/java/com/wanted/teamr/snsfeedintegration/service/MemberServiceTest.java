package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.dto.MemberApprovalRequest;
import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("사용자 서비스 통합 테스트")
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService sut;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("사용자 회원가입 서비스")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @Order(1)
    @Nested
    class Join {

        @DisplayName("계정 이름 중복")
        @Order(2)
        @Test
        void givenDuplicateAccountName_thenThrowsCustomExceptionByDUPLICATE_ACCOUNT_NAME() {
            // given
            MemberJoinRequest dto = MemberJoinRequest.of(ACCOUNT_NAME, EMAIL, PASSWORD);

            // when, then
            Assertions.assertThatThrownBy(() -> sut.join(dto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.DUPLICATE_ACCOUNT_NAME.getMessage());
        }

        @DisplayName("성공")
        @Order(1)
        @Test
        void givenNotDuplicateAccountName_thenSuccess() {
            // given
            MemberJoinRequest dto = MemberJoinRequest.of(ACCOUNT_NAME, EMAIL, PASSWORD);

            // when
            Long memberId = sut.join(dto);

            // then
            Member foundMember = memberRepository.findById(memberId).orElseThrow();
            assertThat(foundMember.getAccountName()).isEqualTo(dto.getAccountName());
            assertThat(foundMember.getEmail()).isEqualTo(dto.getEmail());
            assertThat(passwordEncoder.matches(dto.getPassword(), foundMember.getPassword())).isTrue();
            assertThat(foundMember.getApprovalCode()).hasSize(6);
            assertThat(foundMember.getIsApproved()).isFalse();
        }

    }

    @DisplayName("사용자 가입승인 서비스")
    @Order(2)
    @Nested
    class Approve {

        private String approvalCode;

        @BeforeEach
        void setUp() {
            approvalCode = memberRepository.findById(1L).orElseThrow().getApprovalCode();
        }

        @DisplayName("[계정 이름] 찾을 수 없음")
        @ValueSource(strings = {"myaccoun", "myaccount2"})
        @ParameterizedTest
        void givenNotFoundAccountName_thenThrowsWithACCOUNT_INFO_WRONG(String accountName) {
            // given
            MemberApprovalRequest dto = MemberApprovalRequest.of(accountName, PASSWORD, approvalCode);

            // when, then
            Assertions.assertThatThrownBy(() -> sut.approve(dto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.ACCOUNT_INFO_WRONG.getMessage());
        }

    }

}
