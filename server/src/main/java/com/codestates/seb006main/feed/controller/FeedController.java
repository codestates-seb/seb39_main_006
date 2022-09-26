package com.codestates.seb006main.feed.controller;

import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.feed.dto.FeedDto;
import com.codestates.seb006main.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;

@RestController
@RequestMapping("/api/feeds")
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @PostMapping
    public ResponseEntity postFeed(@RequestBody FeedDto.Post postDto, Authentication authentication) {
        FeedDto.Response responseDto = feedService.createFeed(postDto, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{feed-id}")
    public ResponseEntity getFeed(@PathVariable("feed-id") Long feedId) {
        FeedDto.Response responseDto = feedService.readFeed(feedId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getAllFeeds(@PageableDefault(page = 1, sort = "feedId", direction = Sort.Direction.DESC) Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = feedService.readAllFeeds(pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PatchMapping("/{feed-id}")
    public ResponseEntity patchFeed(@PathVariable("feed-id") Long feedId, @RequestBody FeedDto.Patch patchDto, Authentication authentication) {
        FeedDto.Response responseDto = feedService.updateFeed(feedId, patchDto, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{feed-id}")
    public ResponseEntity deleteFeed(@PathVariable("feed-id") Long feedId, Authentication authentication) {
        feedService.deleteFeed(feedId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
