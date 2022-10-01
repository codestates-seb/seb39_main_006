package com.codestates.seb006main.comment.service;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.comment.dto.CommentDto;
import com.codestates.seb006main.comment.entity.Comment;
import com.codestates.seb006main.comment.mapper.CommentMapper;
import com.codestates.seb006main.comment.repository.CommentRepository;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.feed.entity.Feed;
import com.codestates.seb006main.feed.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final FeedRepository feedRepository;
    private final CommentMapper commentMapper;

    public CommentDto.Response createComment(CommentDto.Post postDto, Authentication authentication) {
        Comment comment = commentMapper.postDtoToComment(postDto);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        comment.setMember(principalDetails.getMember());

        Feed feed = feedRepository.findById(postDto.getFeedId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
        comment.setFeed(feed);

        commentRepository.save(comment);
        return commentMapper.commentToResponseDto(comment);
    }

    public CommentDto.Response updateComment(Long commentId, CommentDto.Patch patchDto, Authentication authentication) {
        feedRepository.findById(patchDto.getFeedId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        checkPermission(comment, authentication);

        comment.updateComment(patchDto.getBody());
        commentRepository.save(comment);
        return commentMapper.commentToResponseDto(comment);
    }

    public void deleteComment(Long commentId, Authentication authentication) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
        checkPermission(comment, authentication);
        commentRepository.deleteById(commentId);
    }

    public void checkPermission(Comment comment, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (comment.getMember().getMemberId() != principalDetails.getMember().getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }
    }
}
