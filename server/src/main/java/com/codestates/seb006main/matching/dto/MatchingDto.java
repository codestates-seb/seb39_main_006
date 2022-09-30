package com.codestates.seb006main.matching.dto;

import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MatchingDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        private String body;

        @Builder
        public Post(String body) {
            this.body = body;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {

    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Long matchingId;
        private String body;
        private Long memberId;
        private String memberName;
        private Long postId;
        private String postTitle;
        private Matching.MatchingStatus matchingStatus;
        private LocalDateTime createdAt;

        @Builder
        public Response(Long matchingId, String body, Member member, Posts posts, Matching.MatchingStatus matchingStatus, LocalDateTime createdAt) {
            this.matchingId = matchingId;
            this.body = body;
            this.memberId = member.getMemberId();
            this.memberName = member.getDisplayName();
            this.postId = posts.getPostId();
            this.postTitle = posts.getTitle();
            this.matchingStatus = matchingStatus;
            this.createdAt = createdAt;
        }
    }
}
