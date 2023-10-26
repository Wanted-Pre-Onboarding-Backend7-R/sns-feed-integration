package com.wanted.teamr.snsfeedintegration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberLogInRequestDto {

    @NotBlank
    private String accountName;

    @NotBlank
    private String password;

}
