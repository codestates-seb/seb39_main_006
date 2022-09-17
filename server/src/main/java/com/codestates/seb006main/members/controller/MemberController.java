package com.codestates.seb006main.members.controller;

import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PatchMapping("/{member-id}")
    public ResponseEntity patchMapping(@PathVariable("member-id") Long memberId,
                                        @RequestBody MemberDto.Patch patch){
        return new ResponseEntity<>(memberService.modifyMember(patch,memberId),HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity checkEmail(@RequestParam String email){
        return new ResponseEntity<>(Map.of("code",memberService.authenticateEmail(email)),HttpStatus.OK);
    }

    @GetMapping("/display-name")
    public ResponseEntity checkDisplayName(@RequestParam String display_name){
        memberService.verifyExistDisplayName(display_name);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
