package com.codestates.seb006main.comment.dto;

import com.codestates.seb006main.feed.entity.Feed;
import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CommentDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post{
        private String body;
        // TODO: feed id가 dto에 포함되는 것이 좋은 방법일까? 주소 매핑이 확실하지 않나?
        private Long feedId;

        @Builder
        public Post(String body, Long feedId) {
            this.body = body;
            this.feedId = feedId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch{
        private String body;

        @Builder
        public Patch(String body, Long feedId) {
            this.body = body;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private Long commentId;
        private String body;
        private Long memberId;
        private String memberName;
        private Long feedId;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        @Builder
        public Response(Long commentId, String body, Member member, Feed feed, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.commentId = commentId;
            this.body = body;
            this.memberId = member.getMemberId();
            this.memberName = member.getDisplayName();
            this.feedId = feed.getFeedId();
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }
}
