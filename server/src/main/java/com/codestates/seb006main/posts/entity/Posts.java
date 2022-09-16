package com.codestates.seb006main.posts.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Posts {
    // Bulletin?
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String title;
    private String body;
    /*
    TODO: image 처리 두가지 방법에 따른 처리
        1. 게시글을 업로드 할 때 이미지를 저장한다면 imageUrl 필드를 선언
        2. 이미지를 게시글에 올리는 시점에 저장소(서버 or 로컬)에 저장해서 imageUrl을 body에 포함하는 방식도 있다.
     */
//    private String imageUrl;
    private PostsStatus postsStatus;
    private LocalDateTime createdAt; // TODO: audit 처리 예정
    private LocalDateTime modifiedAt; // 없어도 되지 않나?
//    private Member member;
    // TODO: Group 매핑 maybe OneToOne
//    private Group group;

    @Builder
    public Posts(Long postId, String title, String body, PostsStatus postsStatus, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.postId = postId;
        this.title = title;
        this.body = body;
        this.postsStatus = postsStatus;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public enum PostsStatus {
        INACTIVE(0, "비활성화"),
        ACTIVE(1, "활성화"),
//        BEFORE_RECRUITMENT(1, "모집 예정"),
        RECRUITING(2, "모집중"),
        COMPLETED(3, "모집완료");

        int stepNumber;
        String postsDescription;

        PostsStatus(int stepNumber, String postsDescription) {
            this.stepNumber = stepNumber;
            this.postsDescription = postsDescription;
        }
    }


    // 비즈니스 로직
    public void updateTitle(String title) {
        this.title = title;
        // TODO: 추후 audit 처리 시 제거
        this.modifiedAt = LocalDateTime.now();
    }

    public void updateBody(String body) {
        this.body = body;
        // TODO: 추후 audit 처리 시 제거
        this.modifiedAt = LocalDateTime.now();
    }
}
