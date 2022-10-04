package com.codestates.seb006main.matching.controller;

import com.codestates.seb006main.matching.dto.MatchingDto;
import com.codestates.seb006main.matching.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RequiredArgsConstructor
@RequestMapping("/api/matching")
@RestController
public class MatchingController {
    private final MatchingService matchingService;

    @PostMapping("/posts/{post-id}")
    public ResponseEntity postMatching(@PathVariable("post-id") @Positive(message = "올바른 번호를 입력해주세요.") Long postId,
                                       @RequestBody @Valid MatchingDto.Post postDto,
                                       Authentication authentication) {
        MatchingDto.Response responseDto = matchingService.createMatching(postId, postDto, authentication);
        return new ResponseEntity(responseDto, HttpStatus.CREATED);
    }

    @GetMapping("/{matching-id}")
    public ResponseEntity getMatching(@PathVariable("matching-id") @Positive(message = "올바른 번호를 입력해주세요.") Long matchingId, Authentication authentication) {
        MatchingDto.Response responseDto = matchingService.readMatching(matchingId, authentication);
        return new ResponseEntity(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{matching-id}/accept")
    public ResponseEntity acceptMatching(@PathVariable("matching-id") @Positive(message = "올바른 번호를 입력해주세요.") Long matchingId, Authentication authentication) {
        MatchingDto.Response responseDto = matchingService.acceptMatching(matchingId, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/{matching-id}/refuse")
    public ResponseEntity refuseMatching(@PathVariable("matching-id") @Positive(message = "올바른 번호를 입력해주세요.") Long matchingId, Authentication authentication) {
        MatchingDto.Response responseDto = matchingService.refuseMatching(matchingId, authentication);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/{matching-id}")
    public ResponseEntity deleteMatching(@PathVariable("matching-id") @Positive(message = "올바른 번호를 입력해주세요.") Long matchingId, Authentication authentication) {
        matchingService.cancelMatching(matchingId, authentication);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
