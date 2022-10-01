package com.codestates.seb006main.matching.mapper;

import com.codestates.seb006main.matching.dto.MatchingDto;
import com.codestates.seb006main.matching.entity.Matching;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchingMapper {
    MatchingDto.Response matchingToResponseDto(Matching matching);
    List<MatchingDto.Response> matchingListToResponseDtoList(List<Matching> matchingList);
}
