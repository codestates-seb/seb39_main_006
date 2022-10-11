package com.codestates.seb006main.posts.controller;

import com.codestates.seb006main.posts.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@RequestMapping("/api/participants")
@RestController
public class MemberPostsController {
    private final PostsService postsService;

    @DeleteMapping("/{memberPosts-id}")
    public ResponseEntity deleteParticipant(@PathVariable("memberPosts-id") @Positive(message = "올바른 번호를 입력해주세요.") Long participantId, Authentication authentication) {
        postsService.deleteParticipant(participantId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
