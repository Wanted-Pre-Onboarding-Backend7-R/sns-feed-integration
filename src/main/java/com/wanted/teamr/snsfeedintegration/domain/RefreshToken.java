package com.wanted.teamr.snsfeedintegration.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Column(name = "refresh_token", columnDefinition = "TEXT", nullable = false)
    private String value;

    @Builder(access = AccessLevel.PRIVATE)
    private RefreshToken(Member member, String value) {
        this.member = member;
        this.value = value;
    }

    public static RefreshToken of (Member member, String value) {
        return RefreshToken.builder()
                .member(member)
                .value(value)
                .build();
    }

}
