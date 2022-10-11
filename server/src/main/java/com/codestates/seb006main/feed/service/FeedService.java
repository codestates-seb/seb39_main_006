package com.codestates.seb006main.feed.service;

import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.Image.repository.ImageRepository;
import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.feed.dto.FeedDto;
import com.codestates.seb006main.feed.entity.Feed;
import com.codestates.seb006main.feed.mapper.FeedMapper;
import com.codestates.seb006main.feed.repository.FeedRepository;
import com.codestates.seb006main.posts.entity.Posts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class FeedService {
    private final FeedRepository feedRepository;
    private final ImageRepository imageRepository;
    private final FeedMapper feedMapper;

    public FeedDto.Response createFeed(FeedDto.Post postDto, Authentication authentication) {
        Feed feed = feedMapper.postDtoToFeed(postDto);
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        feed.setMember(principalDetails.getMember());
        feedRepository.save(feed);

        if (postDto.getImages() != null) {
            saveImages(postDto.getImages(), feed);
        }

        return feedMapper.feedToResponseDto(feed);
    }

    public FeedDto.Response readFeed(Long feedId) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
        return feedMapper.feedToResponseDto(feed);
    }

    public MultiResponseDto readAllFeeds(PageRequest pageRequest) {
        Page<Feed> feedPage = feedRepository.findAll(pageRequest);
        List<Feed> feedList = feedPage.getContent();
        return new MultiResponseDto(feedMapper.feedListToResponseDtoList(feedList), feedPage);
    }

    public FeedDto.Response updateFeed(Long feedId, FeedDto.Patch patchDto, Authentication authentication) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
        checkPermission(feed, authentication);

        // TODO: 사진 수정에 대한 로직
        feed.updateFeed(patchDto.getBody());

        feedRepository.save(feed);
        return feedMapper.feedToResponseDto(feed);
    }

    public void deleteFeed(Long feedId, Authentication authentication) {
        Feed feed = feedRepository.findById(feedId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.FEED_NOT_FOUND));
        checkPermission(feed, authentication);
        feedRepository.deleteById(feedId);
    }

    public void saveImages(List<Long> images, Feed feed) {
        for (Long imageId : images) {
            Image image = imageRepository.findById(imageId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.IMAGE_NOT_FOUND));
            image.setFeed(feed);
            imageRepository.save(image);
        }
    }

    public void checkPermission(Feed feed, Authentication authentication) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        if (feed.getMember().getMemberId() != principalDetails.getMember().getMemberId()) {
            throw new BusinessLogicException(ExceptionCode.PERMISSION_DENIED);
        }
    }
}
