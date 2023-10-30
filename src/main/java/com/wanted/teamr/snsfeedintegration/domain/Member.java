package com.wanted.teamr.snsfeedintegration.domain;

import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.exception.CustomException;
import com.wanted.teamr.snsfeedintegration.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "accountName", callSuper = false)
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

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    private Member(String accountName, String email, String password, String approvalCode, Boolean isApproved) {
        this.accountName = accountName;
        this.email = email;
        this.password = password;
        this.approvalCode = approvalCode;
        this.isApproved = isApproved;
        authority = Authority.ROLE_USER;
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

    public void approve() {
        if (getIsApproved()) {
            throw new CustomException(ErrorCode.ALREADY_APPROVED);
        }
        isApproved = true;
    }

}
