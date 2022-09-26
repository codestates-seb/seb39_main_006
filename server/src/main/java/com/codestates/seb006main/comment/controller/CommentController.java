package com.codestates.seb006main.comment.controller;

import com.codestates.seb006main.comment.dto.CommentDto;
import com.codestates.seb006main.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // TODO: feed id는 어떻게 넘기는 것이 좋을까? 1. dto에 담아서 준다. 2. url 매핑으로 해결한다.

    @PostMapping
    public ResponseEntity postComment(@RequestBody CommentDto.Post postDto, Authentication authentication) {
        CommentDto.Response responseDto = commentService.createComment(postDto, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

//    @GetMapping("/{comment-id}")
//    public ResponseEntity getComment() {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @GetMapping
//    public ResponseEntity getAllComments() {
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PatchMapping("/{comment-id}")
    public ResponseEntity patchComment(@PathVariable("comment-id") Long commentId, @RequestBody CommentDto.Patch patchDto, Authentication authentication) {
        CommentDto.Response responseDto = commentService.updateComment(commentId, patchDto, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") Long commentId, Authentication authentication) {
        commentService.deleteComment(commentId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
