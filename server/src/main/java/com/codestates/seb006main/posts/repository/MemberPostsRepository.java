package com.codestates.seb006main.posts.repository;

import com.codestates.seb006main.posts.entity.MemberPosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPostsRepository extends JpaRepository<MemberPosts,Long> {
}
