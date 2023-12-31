package com.wanted.teamr.snsfeedintegration.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberJoinRequest {

    @NotBlank
    private String accountName;

    @NotBlank
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
            groups = Email.class)
    private String email;

    @NotBlank
    private String password;

    private MemberJoinRequest(String accountName, String email, String password) {
        this.accountName = accountName;
        this.email = email;
        this.password = password;
    }

    public static MemberJoinRequest of(String accountName, String email, String password) {
        return new MemberJoinRequest(accountName, email, password);
    }

}
