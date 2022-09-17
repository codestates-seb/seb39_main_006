package com.codestates.seb006main.members.controller;

import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity postMember(@RequestBody MemberDto.Post post){
        return new ResponseEntity<>(memberService.joinMember(post), HttpStatus.CREATED);
    }

    @GetMapping("/email")
    public ResponseEntity checkEmail(@RequestParam String email){
        return null;
    }

}
