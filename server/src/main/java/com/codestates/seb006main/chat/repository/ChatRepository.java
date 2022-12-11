package com.codestates.seb006main.chat.repository;

import com.codestates.seb006main.chat.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(value = "select * from chat where room_id=:roomId",
            countQuery = "select count(*) from chat where room_id=:roomId",
            nativeQuery = true)
    Page<Chat> loadChatByRoomId(String roomId, PageRequest pageRequest);
}
