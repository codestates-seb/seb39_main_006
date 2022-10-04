package com.codestates.seb006main.matching.entity;

import com.codestates.seb006main.audit.Auditable;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Matching extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long matchingId;
    @Column(length = 500)
    private String body;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;
    @Enumerated(EnumType.STRING)
    private MatchingStatus matchingStatus;

    @Builder
    public Matching(Long matchingId, String body, Member member, Posts posts, MatchingStatus matchingStatus) {
        this.matchingId = matchingId;
        this.body = body;
        this.member = member;
        this.posts = posts;
        this.matchingStatus = matchingStatus == null ? MatchingStatus.NOT_READ : matchingStatus;
    }

    public enum MatchingStatus {
        REFUSED(0,"거절"),
        NOT_READ(1, "읽지 않음"),
        READ(2, "읽음"),
        ACCEPTED(3,"수락");

        private int stepNumber;
        private String matchingDescription;

        MatchingStatus(int stepNumber, String matchingDescription) {
            this.stepNumber = stepNumber;
            this.matchingDescription = matchingDescription;
        }
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public void checkPostsStatus() {
        this.posts.checkStatus();
    }

    public void read() {
        this.matchingStatus = MatchingStatus.READ;
    }

    public void accept() {
        if (this.matchingStatus == MatchingStatus.REFUSED) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_REFUSED);
        }
        if (this.matchingStatus == MatchingStatus.ACCEPTED) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_ACCEPTED);
        }
        if (this.posts.isFull()) {
            throw new BusinessLogicException(ExceptionCode.GROUP_IS_FULL);
        }
        if (this.posts.getParticipants().stream().map(memberPosts -> memberPosts.getMember().getMemberId()).collect(Collectors.toList()).contains(this.member.getMemberId())) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_PARTICIPATED);
        }
        this.matchingStatus = MatchingStatus.ACCEPTED;
    }

    public void refuse() {
        if (this.matchingStatus == MatchingStatus.REFUSED) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_REFUSED);
        }
        this.matchingStatus = MatchingStatus.REFUSED;
    }
}
