package com.wanted.teamr.snsfeedintegration.dto;

import com.wanted.teamr.snsfeedintegration.util.ValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class JoinRequestDTO {

    @NotBlank
    private String accountName;

    @NotBlank
    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
            groups = ValidationGroup.Email.class)
    private String email;

    @NotBlank
    private String password;

}
