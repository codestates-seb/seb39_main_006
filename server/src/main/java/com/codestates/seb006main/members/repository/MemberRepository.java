package com.codestates.seb006main.members.repository;

import com.codestates.seb006main.members.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByDisplayName(String displayName);
    List<Member> findByMemberStatus(Member.MemberStatus memberStatus);
}
