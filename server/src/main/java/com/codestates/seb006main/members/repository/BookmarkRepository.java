package com.codestates.seb006main.members.repository;

import com.codestates.seb006main.members.entity.Bookmark;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.posts.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    Optional<Bookmark> findByMemberAndPost(Member member, Posts posts);
    List<Bookmark> findByMember(Member member);
}
