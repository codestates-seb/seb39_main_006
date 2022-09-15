package com.codestates.seb006main.posts.service;

import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.posts.mapper.PostsMapper;
import com.codestates.seb006main.posts.repository.PostsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;
    private final PostsMapper postsMapper;
    public PostsDto.Response createPosts(PostsDto.Post postDto) {
        Posts posts = postsMapper.postDtoToPosts(postDto);
        postsRepository.save(posts);
        return postsMapper.postsToResponseDto(posts);
    }

    public PostsDto.Response readPosts(Long postId) {
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));
        return postsMapper.postsToResponseDto(posts);
    }

    public MultiResponseDto readAllPosts(PageRequest pageRequest) {
        Page<Posts> postsPage = postsRepository.findAll(pageRequest);
        List<Posts> postsList = postsPage.getContent();
        return new MultiResponseDto<>(postsMapper.postsListToResponseDtoList(postsList), postsPage);
    }

    public PostsDto.Response updatePosts(Long postId, PostsDto.Patch patchDto) {
        Posts posts = postsRepository.findById(postId).orElseThrow(() -> new RuntimeException("존재하지 않는 게시글입니다."));

        Optional.ofNullable(patchDto.getTitle())
                .ifPresent(posts::updateTitle);
        Optional.ofNullable(patchDto.getBody())
                .ifPresent(posts::updateBody);

        postsRepository.save(posts);
        return postsMapper.postsToResponseDto(posts);
    }

    public void deletePosts(Long postId) {
        // TODO: 삭제 대신 게시물을 비활성화 시킨다. 일정 시간이 지나면 삭제를 하도록 처리.
        postsRepository.deleteById(postId);
    }
}
