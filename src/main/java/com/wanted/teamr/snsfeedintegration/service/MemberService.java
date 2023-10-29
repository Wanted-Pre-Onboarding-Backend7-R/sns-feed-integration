package com.wanted.teamr.snsfeedintegration.service;

import com.wanted.teamr.snsfeedintegration.domain.Member;
import com.wanted.teamr.snsfeedintegration.dto.MemberApprovalRequest;
import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import com.wanted.teamr.snsfeedintegration.repository.MemberRepository;
import com.wanted.teamr.snsfeedintegration.util.ApprovalCodeGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(MemberJoinRequest dto) {
        String encodedPassword = passwordEncoder.encode(dto.getPassword());
        String approvalCode = ApprovalCodeGenerator.generate();
        Member member = Member.of(dto, encodedPassword, approvalCode);
        try {
            member = memberRepository.save(member);
            return member.getId();
        } catch (DataIntegrityViolationException e) {
            throw new CustomException(ErrorCode.DUPLICATE_ACCOUNT_NAME, e);
        }
    }

    @Transactional
    public Long approve(MemberApprovalRequest dto) {
        Member member = getMember(dto);
        validatePassword(dto, member);
        validateApprovalCode(dto, member);
        member.approve();
        return member.getId();
    }

    private Member getMember(MemberApprovalRequest dto) {
        return memberRepository.findByAccountName(dto.getAccountName())
                .orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_INFO_WRONG));
    }

    private void validatePassword(MemberApprovalRequest dto, Member member) {
        if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.ACCOUNT_INFO_WRONG);
        }
    }

    private static void validateApprovalCode(MemberApprovalRequest dto, Member member) {
        if (!dto.getApprovalCode().equals(member.getApprovalCode())) {
            throw new CustomException(ErrorCode.APPROVAL_CODE_WRONG);
        }
    }

}
