package com.codestates.seb006main.posts.dto;

import com.codestates.seb006main.group.dto.GroupDto;
import com.codestates.seb006main.posts.entity.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostsDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        // TODO: groupDto.Post를 받거나, posts와 group의 postDto를 둘 다 받는 방식.
        private String title;
        private String body;
        private GroupDto.Post group;

        @Builder
        public Post(String title, String body, GroupDto.Post group) {
            this.title = title;
            this.body = body;
            this.group = group;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch {
        private String title;
        private String body;

        private GroupDto.Patch group;

        @Builder
        public Patch(String title, String body, GroupDto.Patch group) {
            this.title = title;
            this.body = body;
            this.group = group;
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
        // TODO: Group의 id를 꺼내줄 것이냐. group 전체를 보여줄 것이냐.
        private GroupDto.Response group;
//        private Long groupId;

        @Builder
        public Response(Long postId, String title, String body, Posts.PostsStatus postsStatus, LocalDateTime createdAt, LocalDateTime modifiedAt, GroupDto.Response group) {
            this.postId = postId;
            this.title = title;
            this.body = body;
            this.postsStatus = postsStatus;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
            this.group = group;
        }
    }
}
