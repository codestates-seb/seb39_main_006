package com.codestates.seb006main.Image.entity;

import com.codestates.seb006main.feed.entity.Feed;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.Posts;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * TODO: 일정 기간 사용되지 않는 이미지는 삭제 (batch)
 *      -> 중간 비즈니스 로직을 통해 S3에 올라간 데이터까지 삭제
 */

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
    public Image(Long imageId, String originName, String storedName, String storedPath, Long fileSize, Posts posts, Feed feed) {
        this.imageId = imageId;
        this.originName = originName;
        this.storedName = storedName;
        this.storedPath = storedPath;
        this.fileSize = fileSize;
        // TODO: 굳이 생성자에 넣을 필요가 있을까? => 필드를 만들 필요가 있을까? Many 에선 생성만 하고 One에선 조회만 하면 된다.
        this.posts = posts;
        this.feed = feed;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
        if(!posts.getImages().contains(this)) {
            posts.getImages().add(this);
        }
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
        if(!feed.getImages().contains(this))
            feed.getImages().add(this);
    }
}
