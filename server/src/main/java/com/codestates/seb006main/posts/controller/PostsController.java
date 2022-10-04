package com.codestates.seb006main.posts.controller;

import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.posts.dto.PostsCond;
import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.service.PostsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @PostMapping
    public ResponseEntity postPosts(@RequestBody @Valid PostsDto.Post postDto, Authentication authentication) {
        PostsDto.Response responseDto = postsService.createPosts(postDto, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity getPosts(@Positive(message = "올바른 번호를 입력해주세요.") @PathVariable("post-id") Long postId) {
        PostsDto.Response responseDto = postsService.readPosts(postId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAllPosts(@PageableDefault(page = 1, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable,
                                      PostsCond postsCond) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = postsService.readAllPosts(pageRequest, postsCond);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{post-id}/matching")
    public ResponseEntity getAllMatching(@PageableDefault(page = 1, sort = "matchingId", direction = Sort.Direction.DESC) Pageable pageable,
                                         @PathVariable("post-id") @Positive(message = "올바른 번호를 입력해주세요.") Long postId) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = postsService.readAllMatching(postId, pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{post-id}/participants")
    public ResponseEntity getAllParticipants(@PageableDefault(page = 1, sort = "memberPostsId", direction = Sort.Direction.DESC) Pageable pageable,
                                         @PathVariable("post-id") @Positive(message = "올바른 번호를 입력해주세요.") Long postId) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = postsService.readAllParticipants(postId, pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity patchPosts(@PathVariable("post-id") @Positive(message = "올바른 번호를 입력해주세요.") Long postId,
                                     @RequestBody @Valid PostsDto.Patch patchDto,
                                     Authentication authentication) {
        PostsDto.Response responseDto = postsService.updatePosts(postId, patchDto, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity deletePosts(@PathVariable("post-id") @Positive(message = "올바른 번호를 입력해주세요.") Long postId,
                                      Authentication authentication) {
        postsService.deletePosts(postId, authentication);
        return new ResponseEntity<>("정상적으로 삭제되었습니다.", HttpStatus.NO_CONTENT);
    }
}
