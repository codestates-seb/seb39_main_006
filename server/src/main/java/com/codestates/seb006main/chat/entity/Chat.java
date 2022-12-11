package com.codestates.seb006main.chat.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Chat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    private String roomId;
//    private Long senderId;
    private Long senderId;
    private String message;
    private LocalDateTime sendDate;

    @Builder
    public Chat(Long chatId, String roomId, Long senderId, String message) {
        this.chatId = chatId;
        this.roomId = roomId;
        this.senderId = senderId;
        this.message = message;
        this.sendDate = LocalDateTime.now();
    }
}
