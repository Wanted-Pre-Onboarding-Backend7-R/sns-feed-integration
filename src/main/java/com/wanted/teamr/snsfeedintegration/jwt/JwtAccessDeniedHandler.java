package com.wanted.teamr.snsfeedintegration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.teamr.snsfeedintegration.exception.CustomErrorResponse;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 필요한 권한이 존재하지 않은 경우 403 FORBIDDEN
 */
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(new ObjectMapper().writeValueAsString(new CustomErrorResponse(ErrorCode.REQUIRE_AUTHORITY)));
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

}
