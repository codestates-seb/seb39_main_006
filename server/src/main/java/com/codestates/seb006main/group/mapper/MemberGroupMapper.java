package com.codestates.seb006main.group.mapper;

import com.codestates.seb006main.group.dto.GroupDto;
import com.codestates.seb006main.group.entity.Group;
import com.codestates.seb006main.group.entity.MemberGroup;
import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberGroupMapper {
    @Named("memberGroupToGroupResponse")
    default GroupDto.Response memberGroupToGroupResponse(MemberGroup memberGroup){
        Group group =memberGroup.getGroup();
        return GroupDto.Response.builder()
                .groupId(group.getGroupId())
                .closeDate(group.getCloseDate())
                .groupStatus(group.getGroupStatus())
                .headcount(group.getHeadcount())
                .location(group.getLocation())
                .travelPeriod(group.getTravelPeriod())
                .build();
    }

    @Named("memberGroupsToGroupResponseDtos")
    default List<GroupDto.Response> memberGroupsToGroupResponseDtos(List<MemberGroup> memberGroups){
        List<GroupDto.Response> responses = new ArrayList<>();
        for (int i = 0; i < memberGroups.size(); i++) {
            responses.add(memberGroupToGroupResponse(memberGroups.get(i)));
        }
        return responses;
    }

    @Named("memberGroupToMemberResponse")
    default MemberDto.Response memberGroupToMemberResponse(MemberGroup memberGroup){
        Member member = memberGroup.getMember();
        return MemberDto.Response.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .displayName(member.getDisplayName())
                .profileImage(member.getProfileImage())
                .modifiedAt(member.getModifiedAt())
                .role(member.getRole())
                .memberStatus(member.getMemberStatus())
                .createdAt(member.getCreatedAt())
                .content(member.getContent())
                .phone(member.getPhone())
                .build();

    }

    @Named("memberGroupsToMemberResponseDtos")
    default List<MemberDto.Response> memberGroupsToMemberResponseDtos(List<MemberGroup> memberGroups){
        List<MemberDto.Response> responses = new ArrayList<>();
        for (int i = 0; i < memberGroups.size(); i++) {
            responses.add(memberGroupToMemberResponse(memberGroups.get(i)));
        }
        return responses;
    }
}
