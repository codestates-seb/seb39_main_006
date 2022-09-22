package com.codestates.seb006main.members.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.Image.repository.ImageRepository;
import com.codestates.seb006main.Image.service.ImageService;
import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.mail.service.EmailSender;
import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.mapper.MemberMapper;
import com.codestates.seb006main.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    final AmazonS3Client amazonS3Client;
    private final String S3Bucket = "seb-main-006";

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

    public MemberDto.Response modifyMember(MemberDto.Patch patch, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member findMember =principalDetails.getMember();
        String path="";
        if(!patch.getProfileImage().isBlank()){
                path=patch.getProfileImage();
                Image image=imageRepository.findByStoredPath(path).orElseThrow(()->new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
                image.setMember(findMember);
                imageRepository.save(image);
            if(!findMember.getProfileImage().isBlank()){
                String beforePath = findMember.getProfileImage();
                amazonS3Client.deleteObject(S3Bucket,beforePath.substring(beforePath.lastIndexOf("/")+1));
                Image beforeImage=imageRepository.findByStoredPath(beforePath).orElseThrow(()->new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
                imageRepository.delete(beforeImage);
            }
        }
        findMember.updateMember(patch.getDisplayName(),passwordEncoder.encode(patch.getPassword()), patch.getPhone(), patch.getContent(), path, LocalDateTime.now());
        return memberMapper.memberToMemberResponse(memberRepository.save(findMember));
    }

    public MemberDto.Response findMember(Long memberId){
        return memberMapper.memberToMemberResponse(verifyExistMemberWithId(memberId));
    }

    public void withdrawalMember(String password, Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        checkPassword(password, member.getPassword());
        member.setMemberStatus(Member.MemberStatus.WITHDRAWAL);
        memberRepository.save(member);
    }

    public String authenticateEmail(String email){
        verifyExistMemberWithEmail(email);
        String code = createCode();
        try {
            emailSender.sendEmail(email,code);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
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

    private void checkPassword(String password, String encodePassword){
        if(!passwordEncoder.matches(password,encodePassword)){
            throw new BusinessLogicException(ExceptionCode.PASSWORD_NOT_MATCHED);
        }
    }
}