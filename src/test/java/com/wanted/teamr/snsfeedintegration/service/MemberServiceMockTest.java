package com.wanted.teamr.snsfeedintegration.service;

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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("사용자 서비스 Mock 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceMockTest {

    @InjectMocks
    private MemberService sut;
    @Mock
    private MemberRepository memberRepository;

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

    }

}
