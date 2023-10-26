package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.dto.MemberLogInRequestDto;
import com.wanted.teamr.snsfeedintegration.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/api/members/login")
    public ResponseEntity<?> login(@RequestBody MemberLogInRequestDto memberLogInRequestDto, HttpServletResponse response) {
            memberService.login(memberLogInRequestDto, response);
            return new ResponseEntity<>(HttpStatus.OK);
    }

}
