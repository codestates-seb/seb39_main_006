package com.codestates.seb006main.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MemberContoller {

    @PostMapping
    public ResponseEntity postMember(){
        return null;

    }

}
