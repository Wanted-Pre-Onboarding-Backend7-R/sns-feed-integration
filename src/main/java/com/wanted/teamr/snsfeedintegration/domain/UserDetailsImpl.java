package com.wanted.teamr.snsfeedintegration.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserDetailsImpl implements UserDetails {
    private Member member;

    @Builder(access = AccessLevel.PRIVATE)
    private UserDetailsImpl(Member member) {
        this.member = member;
    }

    public static UserDetailsImpl of(Member member) {
        return builder().member(member).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                this.member.getAuthority().name());

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getAccountName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /* 안 잠겼음 */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /* 비밀번호 만료 안됨 */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
