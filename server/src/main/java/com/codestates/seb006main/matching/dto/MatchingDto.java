package com.codestates.seb006main.matching.dto;

import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class MatchingDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        @NotBlank(message = "내용을 입력해주세요.")
        @Size(min = 10, message = "내용은 10자 이상이어야 합니다.")
        @Size(max = 500, message = "내용은 500자를 넘길 수 없습니다.")
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
