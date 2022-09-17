package com.codestates.seb006main.members.service;

import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.mapper.MemberMapper;
import com.codestates.seb006main.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public MemberDto.Response joinMember(MemberDto.Post post){
        if(verifyExistEmail(post.getEmail())) throw new RuntimeException("이미 가입된 이메일입니다");
        Member createdMember =  memberRepository.save(memberMapper.memberPostToMember(post));
        return memberMapper.memberToMemberResponse(createdMember);
    }

    private boolean verifyExistEmail(String email){
        Optional<Member> checkMember =  memberRepository.findByEmail(email);
        return checkMember.isPresent();
    }
    private void verifyExistNickname(String nickname){

    }
}
