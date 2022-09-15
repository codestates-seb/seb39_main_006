package com.codestates.seb006main.posts.dto;

import com.codestates.seb006main.posts.entity.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class PostsDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        private String title;
        private String body;

        //        private Match match;

        @Builder
        public Post(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {
        private String title;
        private String body;

        //        private Match match;

        @Builder
        public Patch(String title, String body) {
            this.title = title;
            this.body = body;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response {
        private Long postId;
        private String title;
        private String body;
//        private String imageUrl;
//        private MultipartFile image;
//        private Long memberId;
//        private String memberName;
        private Posts.PostsStatus postsStatus;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
//        private Match match;

        @Builder
        public Response(Long postId, String title, String body, Posts.PostsStatus postsStatus, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.postId = postId;
            this.title = title;
            this.body = body;
            this.postsStatus = postsStatus;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }
}
