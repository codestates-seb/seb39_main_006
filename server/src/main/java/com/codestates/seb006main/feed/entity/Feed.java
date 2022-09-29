package com.codestates.seb006main.feed.entity;

import com.codestates.seb006main.Image.entity.Image;
import com.codestates.seb006main.audit.Auditable;
import com.codestates.seb006main.comment.entity.Comment;
import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Feed extends Auditable {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedId;
    private String body;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<Comment> comments;
    @OneToMany(mappedBy = "feed", fetch = FetchType.LAZY)
    private List<Image> images;

    @Builder
    public Feed(Long feedId, String body, Member member) {
        this.feedId = feedId;
        this.body = body;
        this.member = member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void updateFeed(String body) {
        this.body = body;
    }
}
