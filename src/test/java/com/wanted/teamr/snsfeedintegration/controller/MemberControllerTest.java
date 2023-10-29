package com.wanted.teamr.snsfeedintegration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.dto.MemberApprovalRequest;
import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@DisplayName("/api/members 통합 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    private static final String BASE_URI = "/api/members";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MemberRepository memberRepository;

    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("/join 사용자 회원가입")
    @Order(1)
    @Nested
    class Join {

        private static final String URI = BASE_URI + "/join";

        @Order(1)
        @DisplayName("성공")
        @Test
        void givenValidInfo_then201() throws Exception {
            doJoin().andExpect(status().isCreated())
                    .andExpect(jsonPath("$").doesNotExist())
                    .andDo(print())
                    .andReturn();
        }

        @Order(2)
        @DisplayName("계정 이름 중복")
        @Test
        void givenDuplicateAccountName_then400() throws Exception {
            ErrorCode errorCode = ErrorCode.DUPLICATE_ACCOUNT_NAME;
            doJoin().andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.errorCode").value(errorCode.name()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                    .andDo(print())
                    .andReturn();
        }

        private ResultActions doJoin() throws Exception {
            MemberJoinRequest dto = MemberJoinRequest.of(ACCOUNT_NAME, EMAIL, PASSWORD);
            String content = mapper.writeValueAsString(dto);
            return mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content));
        }

    }

    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("/approve 사용자 가입승인")
    @Order(2)
    @Nested
    class Approve {

        private static final String URI = BASE_URI + "/approve";

        private String approvalCode;

        @BeforeEach
        void setUp() {
            Member member = memberRepository.findById(1L).orElseThrow();
            approvalCode = member.getApprovalCode();
        }

        @Order(1)
        @DisplayName("성공")
        @Test
        void whenSuccess_then201() throws Exception {
            MemberApprovalRequest dto = MemberApprovalRequest.of(ACCOUNT_NAME, PASSWORD, approvalCode);
            String content = mapper.writeValueAsString(dto);
            mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").doesNotExist())
                    .andDo(print())
                    .andReturn();
        }

        @Order(2)
        @DisplayName("이미 가입승인 완료")
        @Test
        void givenAlreadyApproved_then400() throws Exception {
            MemberApprovalRequest dto = MemberApprovalRequest.of(ACCOUNT_NAME, PASSWORD, approvalCode);
            String content = mapper.writeValueAsString(dto);
            ErrorCode errorCode = ErrorCode.ALREADY_APPROVED;
            mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(errorCode.name()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                    .andDo(print())
                    .andReturn();
        }

    }

}
