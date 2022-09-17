package com.codestates.seb006main.members.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String email;
    private String password;
    private String display_name;
    private String phone;
    private String content;
    private String profileImage;
    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public Member(Long memberId, String email, String password, String display_name, String phone, String content, String profileImage, MemberStatus memberStatus, Role role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.display_name = display_name;
        this.phone = phone;
        this.content = content;
        this.profileImage = profileImage;
        this.memberStatus = memberStatus==null?MemberStatus.ACTIVE:memberStatus;
        this.role = role==null?Role.ROLE_MEMBER:role;
        this.createdAt = createdAt==null?LocalDateTime.now():createdAt;
        this.modifiedAt = modifiedAt==null?LocalDateTime.now():modifiedAt;
    }


    public enum MemberStatus{

        INACTIVE(0,"인증 전"),
        ACTIVE(1,"활동 중"),
        SUSPENSION(2,"정지 계정"),
        SLEEPER(3,"휴면 계정"),
        WITHDRAWAL(4,"삭제");

        private int stepNumber;

        private String memberDescription;

        MemberStatus(int stepNumber, String memberDescription) {
            this.stepNumber = stepNumber;
            this.memberDescription = memberDescription;
        }
    }

    @Getter
    public enum Role {

        ROLE_ADMIN("관리자"), ROLE_MEMBER("일반사용자");

        private String description;

        Role(String description) {
            this.description = description;
        }
    }
}
