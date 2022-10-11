package com.codestates.seb006main.posts.repository;

import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.Posts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Posts, Long>, PostsRepositoryCustom{
    List<Posts> findByPostsStatus(Posts.PostsStatus postsStatus);
    Page<Posts> findByMember(Member member, Pageable pageable);
}
