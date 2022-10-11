package com.codestates.seb006main.posts.entity;

import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberPosts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberPostId;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @Builder
    public MemberPosts(Long memberPostId, Member member, Posts posts) {
        this.memberPostId = memberPostId;
        this.member = member;
        this.posts = posts;
    }

    // TODO: 또 다른 예외가 발생하지 않을까? 예외에 대한 고민을 해보자.
    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    public void checkPostsStatus() {
        this.posts.checkStatus();
    }
}
