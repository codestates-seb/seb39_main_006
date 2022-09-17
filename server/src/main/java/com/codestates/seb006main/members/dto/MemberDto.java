package com.codestates.seb006main.members.dto;

import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        private String email;
        private String password;
        private String display_name;

        @Builder
        public Post(String email, String password, String display_name) {
            this.email = email;
            this.password = password;
            this.display_name = display_name;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private Long memberId;
        private String email;
        private String display_name;
        private String phone;
        private String content;
        private Member.MemberStatus memberStatus;
        private String profileImage;
        private Member.Role role;
        private LocalDateTime createdAt;
        private LocalDateTime modifiedAt;

        @Builder
        public Response(Long memberId, String email, String display_name, String phone, String content, Member.MemberStatus memberStatus, String profileImage, Member.Role role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
            this.memberId = memberId;
            this.email = email;
            this.display_name = display_name;
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
        private String display_name;
        private String password;
        private String phone;
        private String content;
        private String profileImage;

        @Builder
        public Patch( String display_name, String password, String phone, String content, String profileImage) {
            this.display_name = display_name;
            this.password=password;
            this.phone = phone;
            this.content = content;
            this.profileImage = profileImage;
        }
    }
}
