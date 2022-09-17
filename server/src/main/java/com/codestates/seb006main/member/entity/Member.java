package com.codestates.seb006main.member.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    private Long memberId;
    private String email;
    private String password;
    private String phone;
    private String profileImage;
    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus;
    @Enumerated(value = EnumType.STRING)
    private Role role;

    public Member(Long memberId, String email, String password, String phone, String profileImage, MemberStatus memberStatus, Role role) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.profileImage = profileImage;
        this.memberStatus = memberStatus;
        this.role = role;
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
