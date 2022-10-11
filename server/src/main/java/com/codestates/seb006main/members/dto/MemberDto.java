package com.codestates.seb006main.members.dto;

import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.dto.PostsDto;
import com.codestates.seb006main.posts.entity.MemberPosts;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

public class MemberDto {

    @Getter
    @NoArgsConstructor
    public static class Login{
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
        @NotBlank(message = "비밀번호를 입력해주세요.")
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
        @Email(message = "이메일 형식이 아닙니다.")
        @NotBlank(message = "이메일을 입력해주세요.")
        private String email;
        @Setter
        @NotBlank(message = "비밀번호를 입력해주세요.")
        private String password;
        @NotBlank(message = "이름을 입력해주세요.")
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
        @NotBlank(message = "이름을 입력해주세요.")
        private String displayName;
        @NotBlank(message = "비밀번호를 입력해주세요.")
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
    public static class OAuthResponse{
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

        private String access_HH;
        private String refresh_HH;

        public void addToken(String access_HH, String refresh_HH){
            this.access_HH=access_HH;
            this.refresh_HH=refresh_HH;
        }
        @Builder
        public OAuthResponse(Long memberId, String email, String displayName, String phone, String content, Member.MemberStatus memberStatus, String profileImage, Member.Role role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
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
    public static class Participants {
        private Long memberPostId;
        private Long memberId;
        private String displayName;
        private String content;
        private String profileImage;

        @Builder
        public Participants(Long memberPostId, Long memberId, String displayName, String content, String profileImage) {
            this.memberPostId = memberPostId;
            this.memberId = memberId;
            this.displayName = displayName;
            this.content = content;
            this.profileImage = profileImage;
        }
    }
}
