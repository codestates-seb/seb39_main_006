package com.codestates.seb006main.members.dto;

import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.entity.MemberPosts;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class MemberDto {

    @Getter
    @NoArgsConstructor
    public static class Login{
        private String email;
        private String password;

        @Builder
        public Login(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        private String email;
        @Setter
        private String password;
        private String displayName;

        @Builder
        public Post(String email, String password, String displayName) {
            this.email = email;
            this.password = password;
            this.displayName = displayName;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private Long memberId;
        private String email;
        private String displayName;
        private String phone;
        private String content;
        private Member.MemberStatus memberStatus;
        private String profileImage;
        private Member.Role role;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;
        // TODO: Member측에서 참여하고 있는 그룹을 보여줄까?
//        private List<PostsDto.Group> groups;

        @Builder
        public Response(Long memberId, String email, String displayName, String phone, String content, Member.MemberStatus memberStatus, String profileImage, Member.Role role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.memberId = memberId;
            this.email = email;
            this.displayName = displayName;
            this.phone = phone;
            this.content = content;
            this.memberStatus = memberStatus;
            this.profileImage = profileImage;
            this.role = role;
            this.createdAt = createdAt;
            this.modifiedAt = modifiedAt;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Patch{
        private String displayName;
        private String password;
        private String phone;
        private String content;
        private String profileImage;

        @Builder
        public Patch( String displayName, String password, String phone, String content, String profileImage) {
            this.displayName = displayName;
            this.password=password;
            this.phone = phone;
            this.content = content;
            this.profileImage = profileImage;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Participants {
        private Long memberId;
        private String displayName;
        private String content;
        private String profileImage;

        @Builder
        public Participants(Long memberId, String displayName, String content, String profileImage) {
            this.memberId = memberId;
            this.displayName = displayName;
            this.content = content;
            this.profileImage = profileImage;
        }
    }
}
