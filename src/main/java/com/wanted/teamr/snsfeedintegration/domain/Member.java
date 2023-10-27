package com.wanted.teamr.snsfeedintegration.domain;

import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String accountName;

    @Column(nullable = false)
    private String email;

    @Column(length = 60, nullable = false)
    private String password;

    @Column(length = 6, nullable = false)
    private String approvalCode;

    @Column(nullable = false)
    private Boolean isApproved;

    @Builder(access = AccessLevel.PRIVATE)
    public Member(String accountName, String email, String password, String approvalCode, Boolean isApproved) {
        this.accountName = accountName;
        this.email = email;
        this.password = password;
        this.approvalCode = approvalCode;
        this.isApproved = isApproved;
    }

    public static Member of(MemberJoinRequest dto, String encodedPassword, String approvalCode) {
        return builder()
                .accountName(dto.getAccountName())
                .email(dto.getEmail())
                .password(encodedPassword)
                .approvalCode(approvalCode)
                .isApproved(false)
                .build();
    }

}
