package com.codestates.seb006main.chat.repository;

import com.codestates.seb006main.chat.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "select * from chat where room_id=:roomId and send_date < :lastConnected",
            countQuery = "select count(*) from chat where room_id=:roomId and send_date < :lastConnected",
            nativeQuery = true)
    Page<Chat> loadChatByRoomId(String roomId, PageRequest pageRequest, LocalDateTime lastConnected);
}
