package com.codestates.seb006main.members.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.Image.repository.ImageRepository;
import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.mail.service.EmailSender;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.matching.mapper.MatchingMapper;
import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Block;
import com.codestates.seb006main.members.entity.Bookmark;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.members.mapper.MemberMapper;
import com.codestates.seb006main.members.repository.BlockRepository;
import com.codestates.seb006main.members.repository.BookmarkRepository;
import com.codestates.seb006main.members.repository.MemberRepository;
import com.codestates.seb006main.posts.entity.MemberPosts;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.posts.mapper.PostsMapper;
import com.codestates.seb006main.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final PostsMapper postsMapper;
    private final MatchingMapper matchingMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final ImageRepository imageRepository;
    final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;
    @Value("${cloud.aws.s3.default-image}")
    private String defaultImage;
    private final BookmarkRepository bookmarkRepository;
    private final PostsRepository postsRepository;
    private final BlockRepository blockRepository;

    public MemberDto.Response loginMember(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member loginMember = memberRepository.findByEmail(principalDetails.getMember().getEmail())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return memberMapper.memberToMemberResponse(loginMember);
    }


    public MemberDto.Response joinMember(MemberDto.Post post) {
        verifyExistMemberWithEmail(post.getEmail());
        post.setPassword(passwordEncoder.encode(post.getPassword()));
        Member createdMember = memberRepository.save(memberMapper.memberPostToMember(post));
        createdMember.setDefaultImage(defaultImage);
        return memberMapper.memberToMemberResponse(createdMember);
    }

    public MemberDto.Response modifyMember(MemberDto.Patch patch, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member findMember = principalDetails.getMember();
        String path = "";
        if (!patch.getProfileImage().isBlank()) {
            path = patch.getProfileImage();
            Image image = imageRepository.findByStoredPath(path).orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
            image.setMember(findMember);
            imageRepository.save(image);
            if (!findMember.getProfileImage().isBlank()) {
                String beforePath = findMember.getProfileImage();
                if (!beforePath.equals(defaultImage)) {
                    amazonS3Client.deleteObject(S3Bucket, beforePath.substring(beforePath.lastIndexOf("/") + 1));
                    Image beforeImage = imageRepository.findByStoredPath(beforePath).orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
                    imageRepository.delete(beforeImage);
                }
            }
        }
        findMember.updateMember(patch.getDisplayName(), passwordEncoder.encode(patch.getPassword()), patch.getPhone(), patch.getContent(), path, LocalDateTime.now());
        return memberMapper.memberToMemberResponse(memberRepository.save(findMember));
    }

    public MemberDto.Response findMember(Long memberId) {
        return memberMapper.memberToMemberResponse(verifyExistMemberWithId(memberId));
    }

    public void withdrawalMember(String password, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        checkPassword(password, member.getPassword());
        member.setMemberStatus(Member.MemberStatus.WITHDRAWAL);
        memberRepository.save(member);
    }

    public String authenticateEmail(String email) {
        verifyExistMemberWithEmail(email);
        String code = createCode();
        try {
            emailSender.sendEmail(email, code);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return code;
    }

    public void changeBookmark(Long postId, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        Posts post = postsRepository.findActiveById(postId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        Optional<Bookmark> bookmark = bookmarkRepository.findByMemberAndPost(member, post);
        if (bookmark.isPresent()) {
            bookmarkRepository.delete(bookmark.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BOOKMARK_NOT_FOUND)));
        } else {
            bookmarkRepository.save(Bookmark.builder().member(member).post(post).build());
        }
    }

    public void changeBlocked(Long blockedMemberId, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        Optional<Block> block = blockRepository.findByMemberAndBlockedMemberId(member, blockedMemberId);
        if (block.isPresent()) {
            blockRepository.delete(block.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BLOCK_NOT_FOUND)));
        } else {
            blockRepository.save(Block.builder().member(member).blockedMemberId(blockedMemberId).build());
        }
    }

    public Map<String, List<Long>> findMyBookmark(Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = principalDetails.getMember();
        List<Bookmark> bookmarks = bookmarkRepository.findByMember(member);
        List<Long> postIds = new ArrayList<>();
        for (int i = 0; i < bookmarks.size(); i++) {
            postIds.add(bookmarks.get(i).getPost().getPostId());
        }
        return Map.of("postIds", postIds);
    }

    private void verifyExistMemberWithEmail(String email) {
        Optional<Member> checkMember = memberRepository.findByEmail(email);
        if (checkMember.isPresent()) throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }

    private Member verifyExistMemberWithId(Long memberId) {
        Optional<Member> checkMember = memberRepository.findById(memberId);
        return checkMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    public void verifyExistDisplayName(String displayName) {
        Optional<Member> checkMember = memberRepository.findByDisplayName(displayName);
        if (checkMember.isPresent()) throw new BusinessLogicException(ExceptionCode.DISPLAY_NAME_EXISTS);
    }

    private String createCode() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += Integer.toString(random.nextInt(9));
        }
        return result;
    }

    private void checkPassword(String password, String encodePassword) {
        if (!passwordEncoder.matches(password, encodePassword)) {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_NOT_MATCHED);
        }
    }

    public MultiResponseDto readMyPosts(Authentication authentication, PageRequest pageRequest) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberRepository.findById(principalDetails.getMember().getMemberId()).orElseThrow();
        List<Posts> myPostsList = member.getPosts();
        Page<Posts> myPostsPage = new PageImpl<>(myPostsList, pageRequest, myPostsList.size());
        return new MultiResponseDto<>(postsMapper.postsListToResponseDtoList(myPostsList), myPostsPage);
    }

    public MultiResponseDto readBookmarkedPosts(Authentication authentication, PageRequest pageRequest) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberRepository.findById(principalDetails.getMember().getMemberId()).orElseThrow();
        List<Posts> bookmarkedList = member.getBookmarks().stream().map(Bookmark::getPost).collect(Collectors.toList());
        Page<Posts> bookmarkedPage = new PageImpl<>(bookmarkedList, pageRequest, bookmarkedList.size());
        return new MultiResponseDto<>(postsMapper.postsListToResponseDtoList(bookmarkedList), bookmarkedPage);
    }

    public MultiResponseDto readMyMatching(Authentication authentication, PageRequest pageRequest) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberRepository.findById(principalDetails.getMember().getMemberId()).orElseThrow();
        List<Matching> myMatchingList = member.getMatching();
        Page<Matching> myMatchingPage = new PageImpl<>(myMatchingList, pageRequest, myMatchingList.size());
        return new MultiResponseDto<>(matchingMapper.matchingListToResponseDtoList(myMatchingList), myMatchingPage);
    }

    public MultiResponseDto readMyParticipated(Authentication authentication, PageRequest pageRequest) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Member member = memberRepository.findById(principalDetails.getMember().getMemberId()).orElseThrow();
        List<Posts> myParticipatedList = member.getGroups().stream().map(MemberPosts::getPosts).collect(Collectors.toList());
        Page<Posts> myParticipatedPage = new PageImpl<>(myParticipatedList, pageRequest, myParticipatedList.size());
        return new MultiResponseDto<>(postsMapper.postsListToResponseDtoList(myParticipatedList), myParticipatedPage);
    }

    // TODO: 신청 받은 매칭에 대한 조회도 필요할까?
}
