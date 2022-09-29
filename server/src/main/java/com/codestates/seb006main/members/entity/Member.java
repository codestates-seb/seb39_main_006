package com.codestates.seb006main.members.entity;

import com.codestates.seb006main.posts.entity.Posts;
import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.group.entity.MemberGroup;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    private String email;
    private String password;
    private String displayName;
    private String phone;
    private String content;
    private String profileImage;
    @Setter
    @Enumerated(value = EnumType.STRING)
    private MemberStatus memberStatus;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    @OneToMany(mappedBy = "member")
    private List<Posts> posts;
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<MemberGroup> memberGroups;
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks;
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<Block> blocks;
    public void updateMember(String displayName, String password, String phone, String content, String profileImage, LocalDateTime modifiedAt){
        this.displayName=displayName;
        this.password=password;
        this.phone=phone;
        this.content=content;
        this.profileImage=profileImage;
        this.modifiedAt=modifiedAt;
    }

    public Member updateOAuth(String displayName, String profileImage){
        this.displayName=displayName;
        this.profileImage=profileImage;
        return this;
    }

    @Builder
    public Member(Long memberId, String email, String password, String displayName, String phone, String content, String profileImage, MemberStatus memberStatus, Role role, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.memberId = memberId;
        this.email = email;
        this.password = password;
        this.displayName = displayName;
        this.phone = phone;
        this.content = content;
        this.profileImage = profileImage;
        this.memberStatus = memberStatus==null?MemberStatus.ACTIVE:memberStatus;
        this.role = role==null?Role.ROLE_MEMBER:role;
        this.createdAt = createdAt==null?LocalDateTime.now():createdAt;
        this.modifiedAt = modifiedAt==null?LocalDateTime.now():modifiedAt;
    }


    public enum MemberStatus{

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
