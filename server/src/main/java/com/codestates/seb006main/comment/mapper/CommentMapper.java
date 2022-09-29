package com.codestates.seb006main.comment.mapper;

import com.codestates.seb006main.comment.dto.CommentDto;
import com.codestates.seb006main.comment.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    Comment postDtoToComment(CommentDto.Post postDto);
    CommentDto.Response commentToResponseDto(Comment comment);
}
