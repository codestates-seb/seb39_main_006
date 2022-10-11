package com.codestates.seb006main.members.repository;

import com.codestates.seb006main.members.entity.Block;
import com.codestates.seb006main.members.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<Block,Long> {
    Optional<Block> findByMemberAndBlockedMemberId(Member member , Long blockedMemberId);
}
