package com.codestates.seb006main.members.controller;

import com.codestates.seb006main.Image.service.ImageService;
import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.config.redis.RedisUtils;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.jwt.JwtUtils;
import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.service.MemberService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private MemberService memberService;
    private RedisUtils redisUtils;

    public MemberController(MemberService memberService, RedisUtils redisUtils) {
        this.memberService = memberService;
        this.redisUtils = redisUtils;
    }

    @PostMapping("/login")
    public ResponseEntity loginMember(Authentication authentication){
        return new ResponseEntity<>(memberService.loginMember(authentication),HttpStatus.OK);
    }


    @GetMapping("/logout")
    public ResponseEntity logoutMember(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        redisUtils.deleteRefreshToken(principalDetails.getMember().getMemberId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity postMember(@RequestBody MemberDto.Post post){
        return new ResponseEntity<>(memberService.joinMember(post), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity patchMember(Authentication authentication,
                                        @RequestBody MemberDto.Patch patch) {
        return new ResponseEntity<>(memberService.modifyMember(patch,authentication),HttpStatus.OK);
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

    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") Long memberId){
        return new ResponseEntity(memberService.findMember(memberId),HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteMember(@RequestBody String password, Authentication authentication){
        memberService.withdrawalMember(password,authentication);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/bookmark")
    public ResponseEntity bookmark(@RequestParam Long postId, Authentication authentication){
        memberService.changeBookmark(postId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/my-bookmark")
    public ResponseEntity myBookmark(Authentication authentication){
        return new ResponseEntity<>(memberService.findMyBookmark(authentication),HttpStatus.OK);
    }

    @GetMapping("/posts")
    public ResponseEntity getMyPosts(@PageableDefault(page = 1, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable,
                                     Authentication authentication) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = memberService.readMyPosts(authentication, pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/bookmarked")
    public ResponseEntity getBookmarkedPosts(@PageableDefault(page = 1, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable,
                                     Authentication authentication) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = memberService.readBookmarkedPosts(authentication, pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/participated")
    public ResponseEntity getMyParticipated(@PageableDefault(page = 1, sort = "postId", direction = Sort.Direction.DESC) Pageable pageable,
                                            Authentication authentication) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = memberService.readMyParticipated(authentication, pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/matching")
    public ResponseEntity getMyMatching(@PageableDefault(page = 1, sort = "matchingId", direction = Sort.Direction.DESC) Pageable pageable,
                                             Authentication authentication) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = memberService.readMyMatching(authentication, pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    // TODO: 내가 받은 매칭 신청 조회도 필요하지 않을까

    @GetMapping("/blocked")
    public ResponseEntity blockedMember(@RequestParam("blocked-member-id") Long blockedMemberId, Authentication authentication){
        memberService.changeBlocked(blockedMemberId,authentication);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
