package com.codestates.seb006main.Image.entity;

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
    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Posts posts;

    @Builder
    public Image(Long imageId, String originName, String storedName, String storedPath, Long fileSize, Posts posts) {
        this.imageId = imageId;
        this.originName = originName;
        this.storedName = storedName;
        this.storedPath = storedPath;
        this.fileSize = fileSize;
        this.posts = posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
        if(!posts.getImages().contains(this)) {
            posts.getImages().add(this);
        }
    }
}
