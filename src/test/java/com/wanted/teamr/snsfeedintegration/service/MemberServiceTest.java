package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.dto.JoinRequestDTO;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberServiceTest {
    
    @Autowired
    private MemberService sut;
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("사용자 회원가입 서비스")
    @Order(1)
    @Nested
    class Join {
        
        @DisplayName("계정 이름 중복")
        @Test
        void givenDuplicateAccountName_thenThrowsCustomExceptionByDUPLICATE_ACCOUNT_NAME() {
            // given
            JoinRequestDTO dto = new JoinRequestDTO("jeonggoo75", "jeonggoo75@gmail.com", "qlalfqjsgh486^^");
            sut.join(dto);

            // when, then
            Assertions.assertThatThrownBy(() -> sut.join(dto))
                    .isInstanceOf(CustomException.class);
        }

    }
    
}
