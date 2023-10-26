package com.wanted.teamr.snsfeedintegration.controller;

import com.wanted.teamr.snsfeedintegration.dto.JoinRequestDTO;
import com.wanted.teamr.snsfeedintegration.service.MemberService;
import com.wanted.teamr.snsfeedintegration.util.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> join(@Validated(ValidationSequence.class) @RequestBody JoinRequestDTO dto) {
        memberService.join(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
