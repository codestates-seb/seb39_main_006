package com.codestates.seb006main.members.entity;

import com.codestates.seb006main.posts.entity.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;

    @Builder
    public Bookmark(Long bookmarkId, Member member, Posts post) {
        this.bookmarkId = bookmarkId;
        this.member = member;
        this.post = post;
    }
}
