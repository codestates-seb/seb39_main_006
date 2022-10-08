package com.codestates.seb006main.Image.entity;

import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.feed.entity.Feed;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Image {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String originName;
    private String storedName;
    private String storedPath;
    private Long fileSize;
    private LocalDateTime uploadedAt;
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public Image(Long imageId, String originName, String storedName, String storedPath, Long fileSize, Posts posts, Feed feed, LocalDateTime uploadedAt) {
        this.imageId = imageId;
        this.originName = originName;
        this.storedName = storedName;
        this.storedPath = storedPath;
        this.fileSize = fileSize;
        this.posts = posts;
        this.feed = feed;
        this.uploadedAt = uploadedAt;
    }

    public void setPosts(Posts posts) {
        if (posts == null) {
            this.posts = null;
        } else if (this.posts != posts && (this.posts != null || this.member != null || this.feed != null)) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_USED_IMAGE);
        } else {
            this.posts = posts;
        }
    }

    public void setMember(Member member) {
        if (member == null) {
            this.member = null;
        } else if (this.member != member && (this.posts != null || this.member != null || this.feed != null)) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_USED_IMAGE);
        } else {
            this.member = member;
        }
    }

    public void setFeed(Feed feed) {
        if (feed == null) {
            this.feed = null;
        } else if (this.feed != feed && (this.posts != null || this.member != null || this.feed != null)) {
            throw new BusinessLogicException(ExceptionCode.ALREADY_USED_IMAGE);
        } else {
            this.feed = feed;
        }
    }
}
