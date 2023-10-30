package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.dto.*;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.jwt.TokenProvider;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import com.wanted.teamr.snsfeedintegration.util.ApprovalCodeGenerator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public Long join(MemberJoinRequest dto) {
        Member member = createMember(dto);
        try {
            member = memberRepository.save(member);
            return member.getId();
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_ACCOUNT_NAME, e);
        }
    }

    private Member createMember(MemberJoinRequest dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        String approvalCode = ApprovalCodeGenerator.generate();
        return Member.of(dto, encodedPassword, approvalCode);
    }

    @Transactional
    public Long approve(MemberApprovalRequest dto) {
        Member member = getMember(dto);
        validatePassword(dto, member);
        validateApprovalCode(dto, member);
        member.approve();
        return member.getId();
    }

    @Transactional
    public void login(MemberLogInRequest dto, HttpServletResponse response) {
        Member member = getMember(dto);
        validatePassword(dto, member);
        isApproved(member);
        tokenToHeaders(member, response);
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

    private Member getMember(AccountInfo acc) {
        return memberRepository.findByAccountName(acc.getAccountName())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_INFO_WRONG));
    }

    private void validatePassword(AccountInfo accountInfo, Member member) {
        if (!passwordEncoder.matches(accountInfo.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.ACCOUNT_INFO_WRONG);
        }
    }

    private static void validateApprovalCode(MemberApprovalRequest dto, Member member) {
        if (!dto.getApprovalCode().equals(member.getApprovalCode())) {
            throw new CustomException(ErrorCode.APPROVAL_CODE_WRONG);
        }
    }

}
