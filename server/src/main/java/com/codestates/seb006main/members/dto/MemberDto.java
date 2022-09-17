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
        private String nickname;

        @Builder
        public Post(String email, String password, String nickname) {
            this.email = email;
            this.password = password;
            this.nickname = nickname;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response{
        private Long memberId;
        private String email;
        private String nickname;
        private String phone;
        private Member.MemberStatus memberStatus;
        private LocalDateTime createdAt;
        private String profileImage;
        private Member.Role role;

        @Builder
        public Response(Long memberId, String email, String nickname, String phone, Member.MemberStatus memberStatus, LocalDateTime createdAt, String profileImage, Member.Role role) {
            this.memberId = memberId;
            this.email = email;
            this.nickname = nickname;
            this.phone = phone;
            this.memberStatus = memberStatus;
            this.createdAt = createdAt;
            this.profileImage = profileImage;
            this.role = role;
        }
    }
}
