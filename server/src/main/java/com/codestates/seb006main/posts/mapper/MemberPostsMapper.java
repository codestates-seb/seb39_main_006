package com.codestates.seb006main.posts.mapper;


import com.codestates.seb006main.members.dto.MemberDto;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.entity.MemberPosts;
import com.codestates.seb006main.posts.entity.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberPostsMapper {
    @Named("memberPostsToPostsResponse")
    default PostsDto.Response memberPostsToPostsResponse(MemberPosts memberPosts) {
        Posts posts = memberPosts.getPosts();
        return PostsDto.Response.builder()
                .postId(posts.getPostId())
                .title(posts.getTitle())
                .body(posts.getBody())
                .member(posts.getMember())
                .travelPeriod(posts.getTravelPeriod())
                .location(posts.getLocation())
                .totalCount(posts.getTotalCount())
                .closeDate(posts.getCloseDate())
                .postsStatus(posts.getPostsStatus())
                .createdAt(posts.getCreatedAt())
                .modifiedAt(posts.getModifiedAt())
                .build();
    }

    @Named("memberPostsListToPostsResponseList")
    default List<PostsDto.Response> memberPostsListToPostsResponseList(List<MemberPosts> memberPosts) {
        List<PostsDto.Response> responses = new ArrayList<>();
        for (int i = 0; i < memberPosts.size(); i++) {
            responses.add(memberPostsToPostsResponse(memberPosts.get(i)));
        }
        return responses;
    }

    @Named("memberPostsToMemberResponse")
    default MemberDto.Response memberPostsToMemberResponse(MemberPosts memberPosts) {
        Member member = memberPosts.getMember();
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

    @Named("memberPostsListToMemberResponseList")
    default List<MemberDto.Response> memberPostsListToMemberResponseList(List<MemberPosts> memberPosts) {
        List<MemberDto.Response> responses = new ArrayList<>();
        for (int i = 0; i < memberPosts.size(); i++) {
            responses.add(memberPostsToMemberResponse(memberPosts.get(i)));
        }
        return responses;
    }

    @Named("memberPostsToMemberParticipants")
    default MemberDto.Participants memberPostsToMemberParticipants(MemberPosts memberPosts) {
        Member member = memberPosts.getMember();
        return MemberDto.Participants.builder()
                .memberPostId(memberPosts.getMemberPostId())
                .memberId(member.getMemberId())
                .displayName(member.getDisplayName())
                .profileImage(member.getProfileImage())
                .content(member.getContent())
                .build();

    }

    @Named("memberPostsListToMemberParticipantsList")
    default List<MemberDto.Participants> memberPostsListToMemberParticipantsList(List<MemberPosts> memberPosts) {
        List<MemberDto.Participants> responses = new ArrayList<>();
        for (int i = 0; i < memberPosts.size(); i++) {
            responses.add(memberPostsToMemberParticipants(memberPosts.get(i)));
        }
        return responses;
    }
}
