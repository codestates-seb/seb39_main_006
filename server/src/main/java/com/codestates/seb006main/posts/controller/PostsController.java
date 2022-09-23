package com.codestates.seb006main.posts.controller;

import com.codestates.seb006main.dto.MultiResponseDto;
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

import java.io.IOException;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostsController {

    private final PostsService postsService;

    @PostMapping
    public ResponseEntity postPosts(@RequestBody PostsDto.Post postDto, Authentication authentication
//            , @RequestPart(value = "images", required = false) List<MultipartFile> images
                                                                                    ) throws IOException {
        // TODO: groupDto.Post를 받거나, posts와 group의 postDto를 둘 다 받는 방식.
        PostsDto.Response responseDto = postsService.createPosts(postDto, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity getPosts(@PathVariable("post-id") Long postId) {
        PostsDto.Response responseDto = postsService.readPosts(postId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAllPosts(@PageableDefault(page = 1, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = postsService.readAllPosts(pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity patchPosts(@PathVariable("post-id") Long postId,
                                     @RequestBody PostsDto.Patch patchDto,
                                     Authentication authentication) {
        PostsDto.Response responseDto = postsService.updatePosts(postId, patchDto, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity deletePosts(@PathVariable("post-id") Long postId,
                                      Authentication authentication) {
        postsService.deletePosts(postId, authentication);
        return new ResponseEntity<>("정상적으로 삭제되었습니다.", HttpStatus.NO_CONTENT);
    }
}
