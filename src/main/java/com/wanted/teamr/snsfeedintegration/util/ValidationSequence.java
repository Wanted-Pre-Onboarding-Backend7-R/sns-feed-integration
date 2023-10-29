package com.wanted.teamr.snsfeedintegration.util;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;

@GroupSequence({
        Default.class,
        NotBlank.class,
        Email.class})
public interface ValidationSequence {
}
