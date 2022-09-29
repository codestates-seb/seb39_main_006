package com.codestates.seb006main.comment.dto;

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
        private Long feedId;

        @Builder
        public Patch(String body, Long feedId) {
            this.body = body;
            this.feedId = feedId;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private Long commentId;
        private String body;
        private Long memberId;
        private String memberName;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        @Builder
        public Response(Long commentId, String body, Long memberId, String memberName, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.commentId = commentId;
            this.body = body;
            this.memberId = memberId;
            this.memberName = memberName;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }
}
