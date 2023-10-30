package com.wanted.teamr.snsfeedintegration.security;

import com.wanted.teamr.snsfeedintegration.jwt.JwtFilter;
import com.wanted.teamr.snsfeedintegration.jwt.TokenProvider;
import com.wanted.teamr.snsfeedintegration.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * SecurityConfigurerAdapter를 extends 하고 configure 메서드를 오버라이드 하여 JwtFilter를 Security 로직에 적용하는 역할 수행
 */
@RequiredArgsConstructor
@Configuration
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private final TokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * TokenProvider 를 주입받아서 JwtFilter 를 통해 Security 로직에 필터를 등록
     */
    @Override
    public void configure(HttpSecurity httpSecurity) {
        JwtFilter customFilter = new JwtFilter(tokenProvider, userDetailsService);
        /* 위의 JwtFilter를 시큐리티 필터 체인의 한 종류인 UsernamePasswordAuthenticationFilter의 순서보다 앞에서 실행 되게 넣어줌 */
        httpSecurity.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
