package com.wanted.teamr.snsfeedintegration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MemberApprovalRequest {

    @NotBlank
    private String accountName;
    @NotBlank
    private String password;
    @NotBlank
    @Pattern(regexp = "^[0-9A-Za-z]{6}$", groups = Pattern.class)
    private String approvalCode;

    private MemberApprovalRequest(String accountName, String password, String approvalCode) {
        this.accountName = accountName;
        this.password = password;
        this.approvalCode = approvalCode;
    }

    public static MemberApprovalRequest of(String accountName, String password, String approvalCode) {
        return new MemberApprovalRequest(accountName, password, approvalCode);
    }
}
