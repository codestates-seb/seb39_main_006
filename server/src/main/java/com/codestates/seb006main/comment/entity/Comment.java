package com.codestates.seb006main.comment.entity;

import com.codestates.seb006main.audit.Auditable;
import com.codestates.seb006main.feed.entity.Feed;
import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String body;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Comment(Long commentId, String body, Member member, Feed feed) {
        this.commentId = commentId;
        this.body = body;
        this.member = member;
        this.feed = feed;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public void updateComment(String body) {
        this.body = body;
    }
}
