package com.codestates.seb006main.posts.service;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.group.entity.Group;
import com.codestates.seb006main.group.mapper.GroupMapper;
import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.posts.mapper.PostsMapper;
import com.codestates.seb006main.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final PostsMapper postsMapper;
    private final GroupMapper groupMapper;

    public PostsDto.Response createPosts(PostsDto.Post postDto, Authentication authentication) throws IOException {
        Posts posts = postsMapper.postDtoToPosts(postDto);
        Group group = groupMapper.postDtoToGroup(postDto.getGroup());
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        posts.setGroup(group);
        posts.setMember(principalDetails.getMember());
        postsRepository.save(posts);

//        if (images != null){
//            saveImages(images, posts);
//        }

        return postsMapper.postsToResponseDto(posts);
    }

    public PostsDto.Response readPosts(Long postId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
        return postsMapper.postsToResponseDto(posts);
    }

    public MultiResponseDto readAllPosts(PageRequest pageRequest) {
        Page<Posts> postsPage = postsRepository.findAll(pageRequest);
        List<Posts> postsList = postsPage.getContent();
        return new MultiResponseDto<>(postsMapper.postsListToResponseDtoList(postsList), postsPage);
    }

    public PostsDto.Response updatePosts(Long postId, PostsDto.Patch patchDto, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));

        if (posts.getMember().getMemberId() != principalDetails.getMember().getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }

        Optional.ofNullable(patchDto.getTitle())
                .ifPresent(posts::updateTitle);
        Optional.ofNullable(patchDto.getBody())
                .ifPresent(posts::updateBody);
        // TODO: 반영이 되는지 안되는지 테스트.
        Optional.ofNullable(patchDto.getGroup().getCloseDate())
                .ifPresent(posts.getGroup()::updateCloseDate);
        Optional.ofNullable(patchDto.getGroup().getHeadcount())
                .ifPresent(posts.getGroup()::updateHeadcount);

        postsRepository.save(posts);
        return postsMapper.postsToResponseDto(posts);
    }

    public void deletePosts(Long postId, Authentication authentication) {
        // TODO: 삭제 대신 게시물을 비활성화 시킨다. 일정 시간이 지나면 삭제를 하도록 처리.
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (postsRepository.findById(postId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.POST_NOT_FOUND))
                .getMember().getMemberId() != principalDetails.getMember().getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }
        postsRepository.deleteById(postId);
    }

//    public void saveImages(List<MultipartFile> images, Posts posts) throws IOException {
//        for(MultipartFile img : images) {
//            Image image = Image.builder()
//                    .originName(img.getOriginalFilename())
//                    .storedName(fileHandler.storeFile(img))
//                    .storedPath("")
//                    .fileSize(img.getSize())
//                    .build();
////            image.setPosts(posts);
//            imageRepository.save(image);
////            posts.addImage(image);
//        }
//    }
}
