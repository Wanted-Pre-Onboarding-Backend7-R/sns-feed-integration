package com.wanted.teamr.snsfeedintegration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCodeType;
import com.wanted.teamr.snsfeedintegration.exception.RequestBodyErrorCode;
import com.wanted.teamr.snsfeedintegration.security.SecurityConfig;
import com.wanted.teamr.snsfeedintegration.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("/api/members WebMvc 테스트")
@Import(SecurityConfig.class)
@WebMvcTest(MemberController.class)
class MemberControllerMockTest {

    private static final String BASE_URI = "/api/members";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper mapper;

    @DisplayName("사용자 회원가입 WebMvc")
    @Nested
    class Join {

        private static final String URI = BASE_URI + "/join";

        @BeforeEach
        void setUp() {
            given(memberService.join(any(MemberJoinRequest.class)))
                    .willReturn(1L);
        }

        @DisplayName("빈 계정 이름")
        @Test
        void givenBlankAccountName_then400() throws Exception {
            validateRequestBody(BLANK, EMAIL, PASSWORD, RequestBodyErrorCode.ACCOUNT_NAME_BLANK);
        }

        @DisplayName("빈 이메일")
        @Test
        void givenBlankEmail_then400() throws Exception {
            validateRequestBody(ACCOUNT_NAME, BLANK, PASSWORD, RequestBodyErrorCode.EMAIL_BLANK);
        }

        @DisplayName("빈 비밀번호")
        @Test
        void givenBlankPassword_then400() throws Exception {
            validateRequestBody(ACCOUNT_NAME, EMAIL, BLANK, RequestBodyErrorCode.PASSWORD_BLANK);
        }

        @DisplayName("잘못된 이메일 형식")
        @ValueSource(strings = {"wrong_email@", "@a.com", "aa.com", "a.d@s"})
        @ParameterizedTest
        void givenInvalidEmailFormat_then400(String email) throws Exception {
            validateRequestBody(ACCOUNT_NAME, email, PASSWORD, RequestBodyErrorCode.EMAIL_INVALID_FORMAT);
        }

        @DisplayName("[비밀번호] 짧음")
        @ValueSource(strings = {"101sadf", "345^12as", "zxc_123^^"})
        @ParameterizedTest
        void givenShortPassword_then400(String password) throws Exception {
            validateRequestBody(ACCOUNT_NAME, EMAIL, password, ErrorCode.PASSWORD_SHORT);
        }

        @DisplayName("[비밀번호] 3개 이상 같은 문자 반복")
        @ValueSource(strings = {"asdfj3___123", "asdf^###axx", "Asxcvttt90^@"})
        @ParameterizedTest
        void givenPasswordWithRepeatedChar_then400(String password) throws Exception {
            validateRequestBody(ACCOUNT_NAME, EMAIL, password, ErrorCode.PASSWORD_REPEATED_CHAR);
        }

        @DisplayName("[비밀번호] 단순 숫자/영문/특수문자만 (2개 이상 미조합)")
        @ValueSource(strings = {"1238471912398", "aasdfkjhea", "##@@!##!!@!!^"})
        @ParameterizedTest
        void givenSimplePasword_then400(String password) throws Exception {
            validateRequestBody(ACCOUNT_NAME, EMAIL, password, ErrorCode.PASSWORD_SIMPLE);
        }

        @DisplayName("[비밀번호] 개인정보 포함")
        @ValueSource(strings = {EMAIL_HEAD + "123123", ACCOUNT_NAME + "^^"})
        @ParameterizedTest
        void givenPasswordWithPersonalInfo_then400(String password) throws Exception {
            validateRequestBody(ACCOUNT_NAME, EMAIL, password, ErrorCode.PASSWORD_PERSONAL_INFO);
        }

        @DisplayName("성공")
        @Test
        void givenValidInfo_then201() throws Exception {
            MemberJoinRequest dto = new MemberJoinRequest(ACCOUNT_NAME, EMAIL, PASSWORD);
            String content = mapper.writeValueAsString(dto);
            mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$").doesNotExist())
                    .andDo(print())
                    .andReturn();
        }

        private void validateRequestBody(String accountName,
                                         String email,
                                         String password,
                                         ErrorCodeType errorCodeType) throws Exception {
            MemberJoinRequest dto = new MemberJoinRequest(accountName, email, password);
            String content = mapper.writeValueAsString(dto);
            String message = errorCodeType.getMessage();
            mockMvc.perform(post(URI)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(content)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value(errorCodeType.name()))
                    .andExpect(jsonPath("$.message").value(message))
                    .andDo(print())
                    .andReturn();
        }

    }

}