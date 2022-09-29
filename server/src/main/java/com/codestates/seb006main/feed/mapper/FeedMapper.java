package com.codestates.seb006main.feed.mapper;

import com.codestates.seb006main.feed.dto.FeedDto;
import com.codestates.seb006main.feed.entity.Feed;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeedMapper {
    Feed postDtoToFeed(FeedDto.Post postDto);
    FeedDto.Response feedToResponseDto(Feed feed);
    List<FeedDto.Response> feedListToResponseDtoList(List<Feed> feeds);
}
