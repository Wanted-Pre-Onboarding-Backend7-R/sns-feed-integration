package com.wanted.teamr.snsfeedintegration.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class RefreshToken extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @Lob
    @Column(name = "refresh_token", nullable = false)
    private String value;

    public RefreshToken(Member member, String value) {
        this.member = member;
        this.value = value;
    }

}
