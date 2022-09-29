package com.codestates.seb006main.posts.entity;

import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.audit.Auditable;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.util.Period;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Posts extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    private String title;
    private String body;
    @Embedded
    private Period travelPeriod;
    private String location;
    // TODO: 연관관계 매핑된 멤버의 수를 headcount에서 빼면 현재 인원 수가 나온다. -> response로 전달.
    private Integer totalCount;
    private LocalDate closeDate;
    @Enumerated(EnumType.STRING)
    private PostsStatus postsStatus;
//    private LocalDateTime createdAt;
//    private LocalDateTime modifiedAt;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "posts", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MemberPosts> participants = new ArrayList<>();

    /*
    TODO: image 처리 두가지 방법에 따른 처리
        1. 게시글을 업로드 할 때 이미지를 저장한다면 imageUrl 필드를 선언
        2. 이미지를 게시글에 올리는 시점에 저장소(서버 or 로컬)에 저장해서 imageUrl을 body에 포함하는 방식도 있다.
     */
    @OneToMany(mappedBy = "posts", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    // TODO: 조회수, 추천수, 즐겨찾기

    @Builder
    public Posts(Long postId, String title, String body, Member member, Period travelPeriod, String location, Integer totalCount, LocalDate closeDate, PostsStatus postsStatus) {
        this.postId = postId;
        this.title = title;
        this.body = body;
        this.member = member;
        this.travelPeriod = travelPeriod;
        this.location = location;
        this.totalCount = totalCount;
        this.closeDate = closeDate;
        this.postsStatus = Objects.requireNonNullElse(postsStatus, PostsStatus.READY);
    }

    public enum PostsStatus {
        INACTIVE(0, "비활성화"),
        READY(1, "모집 예정"),
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
    public void setMember(Member member) {
        this.member = member;
    }

    public void updatePosts(String title, String body, Integer totalCount, String closeDate) {
        this.title = title;
        this.body = body;
        this.totalCount = totalCount;
        this.closeDate = LocalDate.parse(closeDate);
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateBody(String body) {
        this.body = body;
    }

    public void updateTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public void updateCloseDate(String closeDate) {
        this.closeDate = LocalDate.parse(closeDate);
    }

    public void updateImages() {

    }

    public void inactive() {
        this.postsStatus = PostsStatus.INACTIVE;
    }

    public boolean isFull() {
        return this.participants.size() == totalCount || this.participants.size() > totalCount || this.postsStatus == PostsStatus.COMPLETED;
    }

    public boolean isParticipated(MemberPosts participants) {
        return this.participants.contains(participants);
    }

    public void checkStatus() {
        if (this.participants.size() == this.totalCount) {
            this.postsStatus = PostsStatus.COMPLETED;
        } else if (this.participants.size() > 1) {
            //TODO:
            this.postsStatus = PostsStatus.RECRUITING;
        }
    }

    // TODO: CASCADE 처리, 총 인원보다 많은 인원이 모집되었을 때 처리.
    // TODO: 에외에 대한 것은 굳이 여기서 체크하지 말고 꽉찼는지 안 찾는지는 서비스에서 체크해도 된다.
    public void addMemberPosts(MemberPosts participants) {
        if (isFull()) {
            throw new BusinessLogicException(ExceptionCode.GROUP_IS_FULL);
        }
        if (isParticipated(participants)) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_PARTICIPATED);
        }
        this.participants.add(participants);
        if (participants.getPosts() != this) {
            participants.setPosts(this);
        }
    }

    public void addImage(Image image) {
        this.images.add(image);
        if (image.getPosts() != this) {
            image.setPosts(this);
        }
    }
}
