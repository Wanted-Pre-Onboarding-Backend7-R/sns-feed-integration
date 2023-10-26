package com.wanted.teamr.snsfeedintegration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.dto.JoinRequestDTO;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import com.wanted.teamr.snsfeedintegration.security.SecurityConfig;
import com.wanted.teamr.snsfeedintegration.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("/api/members WebMvc 테스트")
@ImportAutoConfiguration(MessageSourceAutoConfiguration.class)
@Import(SecurityConfig.class)
@WebMvcTest(MemberController.class)
class MemberControllerWebMvcTest {

    private static final String BASE_URI = "/api/members";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ObjectMapper mapper;

    @DisplayName("메시지소스 설정 확인")
    @Test
    void testMessageSource() {
        String message;
        message = messageSource.getMessage("NotBlank", new String[]{"accountName"}, Locale.getDefault());
        Assertions.assertThat(message).isNotNull();
        System.out.println("message = " + message);
    }

    @DisplayName("사용자 회원가입 WebMvc")
    @Nested
    class Join {

        private static final String URI = BASE_URI + "/join";
        private static final String ACCOUNT_NAME = "jeonggoo75";
        private static final String EMAIL = "jeonggoo75@gmail.com";
        private static final String PASSWORD = "qlalfqjsgh486^^";
        private static final String BLANK = "  ";

        @DisplayName("빈 계정 이름")
        @Test
        void givenBlankAccountName_then400() throws Exception {
            testNotBlank(BLANK, EMAIL, PASSWORD, "accountName");
        }

        @DisplayName("빈 이메일")
        @Test
        void givenBlankEmail_then400() throws Exception {
            testNotBlank(ACCOUNT_NAME, BLANK, PASSWORD, "email");
        }

        @DisplayName("빈 비밀번호")
        @Test
        void givenBlankPassword_then400() throws Exception {
            testNotBlank(ACCOUNT_NAME, EMAIL, BLANK, "password");
        }

        void testNotBlank(String accountName, String email, String password, String field) throws Exception {
            JoinRequestDTO dto = new JoinRequestDTO(accountName, email, password);
            String content = mapper.writeValueAsString(dto);
            String message = messageSource.getMessage("NotBlank", new String[]{field}, Locale.getDefault());
            mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(ErrorCode.BLANK_PROPERTY.name()))
                    .andExpect(jsonPath("$.message").value(message))
                    .andDo(print())
                    .andReturn();
        }

        @DisplayName("잘못된 이메일 형식")
        @Test
        void givenInvalidEmailFormat_then400() throws Exception {
            JoinRequestDTO dto = new JoinRequestDTO(ACCOUNT_NAME, "wrong_email@", PASSWORD);
            String content = mapper.writeValueAsString(dto);
            String message = messageSource.getMessage("Email", null, Locale.getDefault());
            mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_EMAIL_FORMAT.name()))
                    .andExpect(jsonPath("$.message").value(message))
                    .andDo(print())
                    .andReturn();
        }

    }

}
