package com.wanted.teamr.snsfeedintegration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
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

@DisplayName("/api/members 통합 테스트")
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    private static final String BASE_URI = "/api/members";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("/join 사용자 회원가입")
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
            MemberJoinRequest dto = new MemberJoinRequest(ACCOUNT_NAME, EMAIL, PASSWORD);
            String content = mapper.writeValueAsString(dto);
            return mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content));
        }

    }
}
