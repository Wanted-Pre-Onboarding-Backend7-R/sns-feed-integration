package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.dto.MemberApprovalRequest;
import com.wanted.teamr.snsfeedintegration.dto.MemberJoinRequest;
import com.wanted.teamr.snsfeedintegration.dto.MemberLogInRequest;
import com.wanted.teamr.snsfeedintegration.service.MemberService;
import com.wanted.teamr.snsfeedintegration.util.PasswordValidator;
import com.wanted.teamr.snsfeedintegration.util.ValidationSequence;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody MemberLogInRequest memberLogInRequest, HttpServletResponse response) {
        memberService.login(memberLogInRequest, response);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@Validated(ValidationSequence.class) @RequestBody MemberJoinRequest dto) {
        PasswordValidator.validatePassword(dto);
        memberService.join(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approve(@Validated(ValidationSequence.class) @RequestBody MemberApprovalRequest dto) {
        memberService.approve(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
