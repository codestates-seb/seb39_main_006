package com.codestates.seb006main.message.repository;

import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, MessageRepositoryCustom {
    Page<Message> findByMember(Member member, Pageable pageable);
}
