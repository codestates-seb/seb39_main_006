package com.codestates.seb006main.posts.entity;

import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.audit.Auditable;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.util.Period;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
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
    @Column(length = 5000)
    private String body;
    @Embedded
    private Period travelPeriod;
    private String location;
    private Integer totalCount;
    private LocalDate closeDate;
    @Enumerated(EnumType.STRING)
    private PostsStatus postsStatus;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "posts", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    private List<MemberPosts> participants = new ArrayList<>();
    @OneToMany(mappedBy = "posts", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Matching> matching = new ArrayList<>();
    @OneToMany(mappedBy = "posts", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();

    // TODO: 조회수

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

    public void deleteImage(Image image) {
        this.images.remove(image);
        image.setPosts(null);
    }

    public void deleteParticipant(MemberPosts memberPosts) {
        this.participants.remove(memberPosts);
        memberPosts.setPosts(null);
    }

    public void inactive() {
        this.postsStatus = PostsStatus.INACTIVE;
    }

    public boolean isFull() {
        return this.participants.size() == totalCount || this.participants.size() > totalCount || this.postsStatus == PostsStatus.COMPLETED;
    }

    public void checkStatus() {
        if (this.participants.size() == this.totalCount) {
            this.postsStatus = PostsStatus.COMPLETED;
        } else {
            this.postsStatus = PostsStatus.RECRUITING;
        }
    }
}
