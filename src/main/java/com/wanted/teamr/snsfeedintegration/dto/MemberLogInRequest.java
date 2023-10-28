package com.wanted.teamr.snsfeedintegration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
public class MemberLogInRequest {

    @NotBlank
    private String accountName;

    @NotBlank
    private String password;
    
    @Builder(access = AccessLevel.PRIVATE)
    private MemberLogInRequest(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    public MemberLogInRequest of(String accountName, String password) {
        return MemberLogInRequest.builder()
                .accountName(accountName)
                .password(password)
                .build();
    }

}
