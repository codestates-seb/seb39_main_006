package com.codestates.seb006main.members.service;

import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
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
        verifyExistEmail(post.getEmail());
        Member createdMember =  memberRepository.save(memberMapper.memberPostToMember(post));
        return memberMapper.memberToMemberResponse(createdMember);
    }

    public void authenticateEmail(){

    }

    private void verifyExistEmail(String email){
        Optional<Member> checkMember =  memberRepository.findByEmail(email);
        if(checkMember.isPresent()) throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }
    private void verifyExistNickname(String nickname){

    }
}
