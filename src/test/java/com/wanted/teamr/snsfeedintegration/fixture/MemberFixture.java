package com.wanted.teamr.snsfeedintegration.fixture;


import com.wanted.teamr.snsfeedintegration.domain.Member;

public final class MemberFixture {

    public static Member MEMBER1() {
        return Member.builder()
                     .accountName("testId")
                     .email("testEmail12321@gmail.com")
                     .password("testpassword123")
                     .approvalCode("cXh9d8")
                     .isApproved(true)
                     .build();
    }
}
