package com.codestates.seb006main.posts.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.Image.repository.ImageRepository;
import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.matching.mapper.MatchingMapper;
import com.codestates.seb006main.posts.dto.PostsCond;
import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.entity.MemberPosts;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.posts.mapper.MemberPostsMapper;
import com.codestates.seb006main.posts.mapper.PostsMapper;
import com.codestates.seb006main.posts.repository.MemberPostsRepository;
import com.codestates.seb006main.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final ImageRepository imageRepository;
    private final PostsMapper postsMapper;
    final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String S3Bucket;
    @Value("${cloud.aws.s3.alter-domain}")
    private String domain;
    private final MemberPostsRepository memberPostsRepository;
    private final MemberPostsMapper memberPostsMapper;
    private final MatchingMapper matchingMapper;

    public PostsDto.Response createPosts(PostsDto.Post postDto, Authentication authentication) {
        Posts posts = postsMapper.postDtoToPosts(postDto);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        posts.setMember(principalDetails.getMember());
        postsRepository.save(posts);

        MemberPosts memberPosts = MemberPosts.builder().member(principalDetails.getMember()).build();
        memberPosts.setPosts(posts);
        memberPostsRepository.save(memberPosts);

        List<String> imagePathList = findImagePathInBody(postDto.getBody());
        if (!imagePathList.isEmpty()) {
            saveImages(imagePathList, posts);
        }

        return postsMapper.postsToResponseDto(posts);
    }

    public PostsDto.Response readPosts(Long postId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        return postsMapper.postsToResponseDto(posts);
    }

    public MultiResponseDto readAllPosts(PageRequest pageRequest, PostsCond postsCond) {
        Page<Posts> postsPage = postsRepository.findAllWithCondition(postsCond, pageRequest);
        List<Posts> postsList = postsPage.getContent();
        return new MultiResponseDto<>(postsMapper.postsListToResponseDtoList(postsList), postsPage);
    }

    public MultiResponseDto readAllMatching(Long postId, PageRequest pageRequest) {
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        List<Matching> matchingList = posts.getMatching().stream().filter(matching -> matching.getMatchingStatus() == Matching.MatchingStatus.READ || matching.getMatchingStatus() == Matching.MatchingStatus.NOT_READ).collect(Collectors.toList());
        Page<Matching> matchingPage = new PageImpl<>(matchingList, pageRequest, matchingList.size());
        return new MultiResponseDto<>(matchingMapper.matchingListToResponseDtoList(matchingList), matchingPage);
    }

    public MultiResponseDto readAllParticipants(Long postId, PageRequest pageRequest) {
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        List<MemberPosts> participantsList = posts.getParticipants();
        Page<MemberPosts> participantsPage = new PageImpl<>(participantsList, pageRequest, participantsList.size());
        return new MultiResponseDto<>(memberPostsMapper.memberPostsListToMemberParticipantsList(participantsList), participantsPage);
    }

    public PostsDto.Response updatePosts(Long postId, PostsDto.Patch patchDto, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));

        if (posts.getMember().getMemberId() != principalDetails.getMember().getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }

        List<String> imagePathList = findImagePathInBody(patchDto.getBody());
        if (!imagePathList.isEmpty()) {
            if (!posts.getImages().isEmpty()) {
                for (int i = 0; i < posts.getImages().size(); i++) {
                    posts.deleteImage(posts.getImages().get(i));
                }
            }
            saveImages(imagePathList, posts);
        }

        posts.updatePosts(patchDto.getTitle(), patchDto.getBody(), patchDto.getTotalCount(), patchDto.getCloseDate());
        postsRepository.save(posts);
        return postsMapper.postsToResponseDto(posts);
    }

    public void deletePosts(Long postId, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Posts posts = postsRepository.findById(postId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        if (posts.getMember().getMemberId() != principalDetails.getMember().getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }
        if (!posts.getImages().isEmpty()) {
            for (Image image : posts.getImages()) {
                String imagePath = image.getStoredPath();
                amazonS3Client.deleteObject(S3Bucket, imagePath.substring(imagePath.lastIndexOf("/") + 1));
                posts.deleteImage(image);
                imageRepository.delete(image);
            }
        }
        posts.inactive();
        postsRepository.save(posts);
    }

    public void deleteParticipant(Long participantId, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        MemberPosts memberPosts = memberPostsRepository.findById(participantId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.PARTICIPANT_NOT_FOUND));
        Posts posts = postsRepository.findById(memberPosts.getPosts().getPostId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        if (posts.getMember().getMemberId() != principalDetails.getMember().getMemberId() &&
                memberPosts.getMember().getMemberId() != principalDetails.getMember().getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }
        memberPostsRepository.deleteById(participantId);
        posts.checkStatus();
        postsRepository.save(posts);
    }

    public void saveImages(List<String> imagePathList, Posts posts) {
        for (String imagePath : imagePathList) {
            Image image = imageRepository.findByStoredPath(imagePath).orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
            image.setPosts(posts);
            imageRepository.save(image);
        }
    }

    public List<String> findImagePathInBody(String body) {
        List<String> imagePathList = new ArrayList<>();
            while (body.contains(domain)) {
                body = body.substring(body.indexOf(domain));
                int startIdx = 0;
                int endIdx = body.indexOf(')');
                String imagePath = "https://" + body.substring(startIdx, endIdx);
                imagePathList.add(imagePath);
                body = body.substring(endIdx);
            }
            return imagePathList;
    }
}
