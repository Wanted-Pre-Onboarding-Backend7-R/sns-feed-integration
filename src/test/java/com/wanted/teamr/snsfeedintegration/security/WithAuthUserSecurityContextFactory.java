package com.wanted.teamr.snsfeedintegration.security;

import com.wanted.teamr.snsfeedintegration.domain.Authority;
import com.wanted.teamr.snsfeedintegration.domain.UserDetailsImpl;
import com.wanted.teamr.snsfeedintegration.fixture.JwtFixture;
import com.wanted.teamr.snsfeedintegration.fixture.MemberFixture;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.Collection;

public class WithAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithAuthUser> {

    @Override
    public SecurityContext createSecurityContext(WithAuthUser annotation) {
        // TODO: 고정된 member entity 말고 지정할 수 있게 수정
        // TODO: role도 고정된 값 말고 토큰에 있는 값 파싱에서 적용하게 수정

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(Authority.ROLE_USER.toString());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(UserDetailsImpl.of(MemberFixture.MEMBER1()), JwtFixture.VAILD_AT_MEMBER1, authorities);
        
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        return context;
    }
}
