package com.wanted.teamr.snsfeedintegration.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;

@Entity
@Getter
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

}
