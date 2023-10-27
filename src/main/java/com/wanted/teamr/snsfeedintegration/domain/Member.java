package com.wanted.teamr.snsfeedintegration.domain;

import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder(access = AccessLevel.PRIVATE)
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

    public static Member of(MemberJoinRequest dto, String encodedPassword, String approvalCode) {
        return builder().accountName(dto.getAccountName())
                .email(dto.getEmail())
                .password(encodedPassword)
                .approvalCode(approvalCode)
                .isApproved(false)
                .build();
    }
}
