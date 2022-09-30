package com.codestates.seb006main.matching.controller;

import com.codestates.seb006main.matching.dto.MatchingDto;
import com.codestates.seb006main.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/matching")
@RestController
public class MatchingController {
    private final MatchingService matchingService;

    @PostMapping("/posts/{post-id}")
    public ResponseEntity postMatching(@PathVariable("post-id") Long postId, Authentication authentication) {
        MatchingDto.Response responseDto = matchingService.createMatching(postId, authentication);
        return new ResponseEntity(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{matching-id}")
    public ResponseEntity readMatching(@PathVariable("matching-id") Long matchingId, Authentication authentication) {
        MatchingDto.Response responseDto = matchingService.readMatching(matchingId, authentication);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{matching-id}/accept")
    public ResponseEntity acceptMatching(@PathVariable("matching-id") Long matchingId, Authentication authentication) {
        matchingService.acceptMatching(matchingId, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{matching-id}/refuse")
    public ResponseEntity refuseMatching(@PathVariable("matching-id") Long matchingId, Authentication authentication) {
        matchingService.refuseMatching(matchingId, authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{matching-id}")
    public ResponseEntity deleteMatching(@PathVariable("matching-id") Long matchingId, Authentication authentication) {
        matchingService.cancelMatching(matchingId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
