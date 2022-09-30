package com.codestates.seb006main.feed.dto;

import com.codestates.seb006main.comment.dto.CommentDto;
import com.codestates.seb006main.comment.entity.Comment;
import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class FeedDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post{
        private String body;
        private List<Long> images;

        @Builder
        public Post(String body, List<Long> images) {
            this.body = body;
            this.images = images;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {
        private String body;
        private List<Long> images;

        @Builder
        public Patch(String body, List<Long> images) {
            this.body = body;
            this.images = images;

        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private Long feedId;
        private String body;
        private Long memberId;
        private String memberName;
        private String profileImage;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        private List<CommentDto.Response> comments;

        @Builder
        public Response(Long feedId, String body, Member member, LocalDateTime createdAt, LocalDateTime modifiedAt, List<CommentDto.Response> comments) {
            this.feedId = feedId;
            this.body = body;
            this.memberId = member.getMemberId();
            this.memberName = member.getDisplayName();
            this.profileImage = member.getProfileImage();
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
            this.comments = comments;
        }
    }
}
