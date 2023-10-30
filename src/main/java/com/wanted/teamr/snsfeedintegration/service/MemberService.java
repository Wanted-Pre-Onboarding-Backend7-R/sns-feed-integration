package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.dto.MemberLogInRequest;
import com.wanted.teamr.snsfeedintegration.dto.TokenDto;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.jwt.TokenProvider;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public void login(MemberLogInRequest memberLogInRequest, HttpServletResponse response) {
        Member member = isPresentMember(memberLogInRequest.getAccountName());
        validatePassword(memberLogInRequest, member);
        isApproved(member);
        tokenToHeaders(member, response);
    }

    public Member isPresentMember(String accountName) {
        return memberRepository.findByAccountName(accountName)
                .orElseThrow(() -> new CustomException(ErrorCode.WRONG_ACCOUNT_INFO));
    }

    public void validatePassword(MemberLogInRequest memberLogInRequest, Member member) {
        if(!bCryptPasswordEncoder.matches(memberLogInRequest.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.WRONG_ACCOUNT_INFO);
        }
    }

    public void isApproved(Member member) {
        if(!member.getIsApproved()) {
            throw new CustomException(ErrorCode.REQUIRE_APPROVAL);
        }
    }

    public void tokenToHeaders(Member member, HttpServletResponse response) {
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

}
