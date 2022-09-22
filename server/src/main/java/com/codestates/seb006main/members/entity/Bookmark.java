package com.codestates.seb006main.members.entity;

import com.codestates.seb006main.posts.entity.Posts;

import javax.persistence.*;

@Entity
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookmarkId;
    @ManyToOne(targetEntity = Member.class)
    private Long memberId;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;
}
