package com.wanted.teamr.snsfeedintegration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.dto.MemberLogInRequest;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.RefreshTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class MemberControllerTest {

    private static final String BASE_URI = "/api/members";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private ObjectMapper mapper;

    @DisplayName("사용자 로그인")
    @Nested
    class Login {
        private static final String URI = BASE_URI + "/login";

        @AfterEach
        void delete() {
            refreshTokenRepository.deleteAll();
        }

        @Order(1)
        @DisplayName("로그인 성공시 응답")
        @Test
        void login() throws Exception {
            MemberLogInRequest dto = MemberLogInRequest.of("name1234","12345678");
            String content = mapper.writeValueAsString(dto);
            mockMvc.perform(post(URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content))
                    .andExpect(status().isCreated())
                    .andDo(print())
                    .andReturn();

        }

        @Order(2)
        @DisplayName("로그인 실패시 응답 - acoountname 존재하지 않음")
        @Test
        void loginfailedbyNotFound() throws Exception {
            MemberLogInRequest dto = MemberLogInRequest.of("name","12345678");
            String content = mapper.writeValueAsString(dto);
            ErrorCode errorCode = ErrorCode.WRONG_ACCOUNT_INFO;
            mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value(errorCode.name()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                    .andDo(print())
                    .andReturn();

        }

        @Order(3)
        @DisplayName("로그인 실패시 응답 - 비밀번호 다름")
        @Test
        void loginfailedbyWrongPassword() throws Exception {
            MemberLogInRequest dto = MemberLogInRequest.of("name1234","11111111");
            String content = mapper.writeValueAsString(dto);
            ErrorCode errorCode = ErrorCode.WRONG_ACCOUNT_INFO;
            mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errorCode").value(errorCode.name()))
                    .andExpect(jsonPath("$.message").value(errorCode.getMessage()))
                    .andDo(print())
                    .andReturn();

        }

    }

}