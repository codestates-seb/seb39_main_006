package com.codestates.seb006main.posts.mapper;

import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.entity.Posts;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostsMapper {
    Posts postDtoToPosts(PostsDto.Post postDto);
    Posts patchDtoToPosts(PostsDto.Patch patchDto);
    PostsDto.Response postsToResponseDto(Posts posts);
    List<PostsDto.Response> postsListToResponseDtoList(List<Posts> postsList);
}
