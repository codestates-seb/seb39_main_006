package com.codestates.seb006main.members.mapper;

import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {
    Member memberPostToMember(MemberDto.Post post);
    MemberDto.Response memberToMemberResponse(Member member);
    Member memberPatchToMember(MemberDto.Patch patch);
}
