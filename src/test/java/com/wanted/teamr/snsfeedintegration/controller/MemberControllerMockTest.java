package com.wanted.teamr.snsfeedintegration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.dto.MemberApprovalRequest;
import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCodeType;
import com.wanted.teamr.snsfeedintegration.exception.RequestBodyErrorCode;
import com.wanted.teamr.snsfeedintegration.security.JwtSecurityConfig;
import com.wanted.teamr.snsfeedintegration.jwt.JwtAccessDeniedHandler;
import com.wanted.teamr.snsfeedintegration.jwt.JwtAuthenticationEntryPoint;
import com.wanted.teamr.snsfeedintegration.jwt.TokenProvider;
import com.wanted.teamr.snsfeedintegration.security.SecurityConfig;
import com.wanted.teamr.snsfeedintegration.service.MemberService;
import com.wanted.teamr.snsfeedintegration.service.UserDetailsServiceImpl;
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

import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.ACCOUNT_NAME;
import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.APPROVAL_CODE;
import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.BLANK;
import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.EMAIL;
import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.EMAIL_HEAD;
import static com.wanted.teamr.snsfeedintegration.controller.TestConstants.PASSWORD;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("/api/members WebMvc")
@Import(SecurityConfig.class)
@MockBean(JwtSecurityConfig.class)
@WebMvcTest(MemberController.class)
class MemberControllerMockTest {

    private static final String BASE_URI = "/api/members";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserDetailsServiceImpl userDetailsServiceImpl;
    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @DisplayName("/join 사용자 회원가입 WebMvc")
    @Nested
    class Join {

        private static final String URI = BASE_URI + "/join";

        @BeforeEach
        void setUp() {
            given(memberService.join(any(MemberJoinRequest.class)))
                    .willReturn(1L);
        }

        @DisplayName("[계정 이름] 공백")
        @Test
        void givenBlankAccountName_then400() throws Exception {
            validateRequestBody(BLANK, EMAIL, PASSWORD, RequestBodyErrorCode.ACCOUNT_NAME_BLANK);
        }

        @DisplayName("[이메일] 공백")
        @Test
        void givenBlankEmail_then400() throws Exception {
            validateRequestBody(ACCOUNT_NAME, BLANK, PASSWORD, RequestBodyErrorCode.EMAIL_BLANK);
        }

        @DisplayName("[비밀번호] 공백")
        @Test
        void givenBlankPassword_then400() throws Exception {
            validateRequestBody(ACCOUNT_NAME, EMAIL, BLANK, RequestBodyErrorCode.PASSWORD_BLANK);
        }

        @DisplayName("[이메일] 잘못된 형식")
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
            MemberJoinRequest dto = MemberJoinRequest.of(ACCOUNT_NAME, EMAIL, PASSWORD);
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
            MemberJoinRequest dto = MemberJoinRequest.of(accountName, email, password);
            MemberControllerMockTest.this.validateRequestBody(dto, errorCodeType, URI);
        }

    }

    @DisplayName("/approve 사용자 가입승인 WebMvc")
    @Nested
    class Approve {

        private static final String URI = BASE_URI + "/approve";

        @BeforeEach
        void setUp() {
            given(memberService.approve(any(MemberApprovalRequest.class)))
                    .willReturn(1L);
        }

        @DisplayName("[계정 이름] 공백")
        @Test
        void givenBlankAccountName_then400() throws Exception {
            validateRequestBody(BLANK, PASSWORD, APPROVAL_CODE, RequestBodyErrorCode.ACCOUNT_NAME_BLANK);
        }

        @DisplayName("[비밀번호] 공백")
        @Test
        void givenBlankPassword_then400() throws Exception {
            validateRequestBody(ACCOUNT_NAME, BLANK, APPROVAL_CODE, RequestBodyErrorCode.PASSWORD_BLANK);
        }

        @DisplayName("[승인코드] 공백")
        @Test
        void givenBlankApprovalCode_then400() throws Exception {
            validateRequestBody(ACCOUNT_NAME, PASSWORD, BLANK, RequestBodyErrorCode.APPROVAL_CODE_BLANK);
        }

        @DisplayName("[승인코드] 유효하지 않은 형식")
        @ParameterizedTest
        @ValueSource(strings = {"1235A", "1235AAA", "sd12A&"})
        void givenInvalidApprovalCodeFormat_then400(String approvalCode) throws Exception {
            validateRequestBody(ACCOUNT_NAME, PASSWORD, approvalCode, RequestBodyErrorCode.APPROVAL_CODE_INVALID_FORMAT);
        }

        private void validateRequestBody(String accountName,
                                         String password,
                                         String approvalCode,
                                         ErrorCodeType errorCodeType) throws Exception {
            MemberApprovalRequest dto = MemberApprovalRequest.of(accountName, password, approvalCode);
            MemberControllerMockTest.this.validateRequestBody(dto, errorCodeType, URI);
        }

    }

    private void validateRequestBody(Object dto,
                                     ErrorCodeType errorCodeType,
                                     String uri) throws Exception {
        String content = mapper.writeValueAsString(dto);
        String message = errorCodeType.getMessage();
        mockMvc.perform(post(uri)
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
