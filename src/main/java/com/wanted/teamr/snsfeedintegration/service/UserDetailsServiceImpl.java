package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.domain.UserDetailsImpl;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String accountName) {
        Member member = memberRepository.findByAccountName(accountName)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return UserDetailsImpl.of(member);

    }

}
