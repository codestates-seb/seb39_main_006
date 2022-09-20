package com.codestates.seb006main.members.service;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.mapper.MemberMapper;
import com.codestates.seb006main.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberDto.Response loginMember(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member loginMember = memberRepository.findByEmail(principalDetails.getMember().getEmail())
                .orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return memberMapper.memberToMemberResponse(loginMember);
    }

    public MemberDto.Response joinMember(MemberDto.Post post){
        verifyExistMemberWithEmail(post.getEmail());
        post.setPassword(passwordEncoder.encode(post.getPassword()));
        Member createdMember =  memberRepository.save(memberMapper.memberPostToMember(post));
        return memberMapper.memberToMemberResponse(createdMember);
    }

    public MemberDto.Response modifyMember(MemberDto.Patch patch, Long memberId){
        Member findMember =verifyExistMemberWithId(memberId);
        //프로필 이미지 처리할 방법 생각할 것
        findMember.updateMember(patch.getDisplayName(),patch.getPassword(), patch.getPhone(), patch.getContent(), patch.getProfileImage(), LocalDateTime.now());
        return memberMapper.memberToMemberResponse(memberRepository.save(findMember));
    }

    public MemberDto.Response findMember(Long memberId){
        return memberMapper.memberToMemberResponse(verifyExistMemberWithId(memberId));
    }

    public void withdrawalMember(Long memberId){
        Member findMember = verifyExistMemberWithId(memberId);
        findMember.setMemberStatus(Member.MemberStatus.WITHDRAWAL);
        memberRepository.save(findMember);
    }

    public String authenticateEmail(String email){
        verifyExistMemberWithEmail(email);
        String code = createCode();
        //이메일 발송 로직 추가해야함.
        return code;
    }

    private void verifyExistMemberWithEmail(String email){
        Optional<Member> checkMember =  memberRepository.findByEmail(email);
        if(checkMember.isPresent()) throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }

    private Member verifyExistMemberWithId(Long memberId){
        Optional<Member> checkMember = memberRepository.findById(memberId);
        return checkMember.orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
    public void verifyExistDisplayName(String displayName){
        Optional<Member> checkMember = memberRepository.findByDisplayName(displayName);
        if(checkMember.isPresent()) throw new BusinessLogicException(ExceptionCode.DISPLAY_NAME_EXISTS);
    }

    private String createCode(){
        Random random = new Random();
        String result = "";
        for(int i =0; i<6; i++){
            result+=Integer.toString(random.nextInt(9));
        }
        return result;
    }
}
