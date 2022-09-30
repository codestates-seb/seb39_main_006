package com.codestates.seb006main.posts.repository;

import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.MemberPosts;
import com.codestates.seb006main.posts.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberPostsRepository extends JpaRepository<MemberPosts,Long> {
    Optional<MemberPosts> findByMemberAndPosts(Member member, Posts posts);
}
