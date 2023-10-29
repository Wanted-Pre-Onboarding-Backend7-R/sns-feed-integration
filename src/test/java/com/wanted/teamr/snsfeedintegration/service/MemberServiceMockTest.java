package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.dto.MemberApprovalRequest;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@DisplayName("사용자 서비스 Mock 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceMockTest {

    @InjectMocks
    private MemberService sut;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName("사용자 가입승인 서비스 Mock 테스트")
    @Nested
    class Approve {

        @DisplayName("[계정 이름] 찾을 수 없음")
        @Test
        void givenNotFoundAccountName_thenThrowsWithACCOUNT_INFO_WRONG() {
            // given
            given(memberRepository.findByAccountName(anyString()))
                    .willReturn(Optional.empty());

            MemberApprovalRequest dto = mock(MemberApprovalRequest.class);
            String wrongAccountName = "wrongAccountName";
            given(dto.getAccountName())
                    .willReturn(wrongAccountName);

            // when, then
            Assertions.assertThatThrownBy(() -> sut.approve(dto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.ACCOUNT_INFO_WRONG.getMessage());
        }

        @DisplayName("[비밀번호] 불일치")
        @Test
        void givenWrongPassword_thenThrowsWithACCOUNT_INFO_WRONG() {
            // given
            Member member = mock(Member.class);
            given(memberRepository.findByAccountName(anyString()))
                    .willReturn(Optional.of(member));

            MemberApprovalRequest dto = mock(MemberApprovalRequest.class);
            given(dto.getAccountName())
                    .willReturn(ACCOUNT_NAME);

            given(member.getPassword())
                    .willReturn(PASSWORD);
            given(dto.getPassword())
                    .willReturn(PASSWORD);

            given(passwordEncoder.matches(dto.getPassword(), member.getPassword()))
                    .willReturn(false);

            // when, then
            Assertions.assertThatThrownBy(() -> sut.approve(dto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.ACCOUNT_INFO_WRONG.getMessage());
        }

        @DisplayName("[승인코드] 불일치")
        @Test
        void givenWrongApprovalCode_thenThrowsWithAPPROVAL_CODE_WRONG() {
            // given
            Member member = mock(Member.class);
            given(memberRepository.findByAccountName(anyString()))
                    .willReturn(Optional.of(member));

            MemberApprovalRequest dto = mock(MemberApprovalRequest.class);
            given(dto.getAccountName())
                    .willReturn(ACCOUNT_NAME);
            given(member.getPassword())
                    .willReturn(PASSWORD);
            given(dto.getPassword())
                    .willReturn(PASSWORD);
            given(passwordEncoder.matches(dto.getPassword(), member.getPassword()))
                    .willReturn(true);
            given(dto.getApprovalCode())
                    .willReturn("wrongApprovalCode");
            given(member.getApprovalCode())
                    .willReturn(APPROVAL_CODE);

            // when, then
            Assertions.assertThatThrownBy(() -> sut.approve(dto))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.APPROVAL_CODE_WRONG.getMessage());
        }

        @DisplayName("이미 가입승인 완료")
        @Test
        void givenJoinHaveAlreadyApproved_thenThrowsWithALREADY_APPROVED() {
            // given
            MemberApprovalRequest dto = mock(MemberApprovalRequest.class);
            Member member = mock(Member.class);

            given(dto.getAccountName())
                    .willReturn(ACCOUNT_NAME);
            given(memberRepository.findByAccountName(anyString()))
                    .willReturn(Optional.of(member));

            given(member.getPassword())
                    .willReturn(PASSWORD);
            given(dto.getPassword())
                    .willReturn(PASSWORD);
            given(passwordEncoder.matches(dto.getPassword(), member.getPassword()))
                    .willReturn(true);

            given(dto.getApprovalCode())
                    .willReturn(APPROVAL_CODE);
            given(member.getApprovalCode())
                    .willReturn(APPROVAL_CODE);

            doThrow(CustomException.class)
                    .when(member).approve();

            // when, then
            Assertions.assertThatThrownBy(() -> sut.approve(dto))
                    .isInstanceOf(CustomException.class);
        }

        @DisplayName("성공")
        @Test
        void whenSuccess_thenMemberApproveCalled() {
            // given
            MemberApprovalRequest dto = mock(MemberApprovalRequest.class);
            Member member = mock(Member.class);

            given(dto.getAccountName())
                    .willReturn(ACCOUNT_NAME);
            given(memberRepository.findByAccountName(anyString()))
                    .willReturn(Optional.of(member));

            given(member.getPassword())
                    .willReturn(PASSWORD);
            given(dto.getPassword())
                    .willReturn(PASSWORD);
            given(passwordEncoder.matches(dto.getPassword(), member.getPassword()))
                    .willReturn(true);

            given(dto.getApprovalCode())
                    .willReturn(APPROVAL_CODE);
            given(member.getApprovalCode())
                    .willReturn(APPROVAL_CODE);

            // when, then
            sut.approve(dto);

            // then
            then(member).should(times(1)).approve();
        }

    }

}
