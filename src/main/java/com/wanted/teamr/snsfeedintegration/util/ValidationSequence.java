package com.wanted.teamr.snsfeedintegration.util;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, ValidationGroup.NotBlank.class, ValidationGroup.Email.class})
public interface ValidationSequence {
}
