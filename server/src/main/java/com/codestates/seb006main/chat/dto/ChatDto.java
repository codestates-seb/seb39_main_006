package com.codestates.seb006main.chat.dto;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

public class ChatDto {
    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Message {
        private String roomId;
        private String writer;
        private String message;

        @Builder
        public Message(String roomId, String writer, String message) {
            this.roomId = roomId;
            this.writer = writer;
            this.message = message;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Room {
        private String roomId;
        private String name;
        private Set<WebSocketSession> sessions = new HashSet<>();

        @Builder
        public Room(String roomId, String name, Set<WebSocketSession> sessions) {
            this.roomId = roomId;
            this.name = name;
            this.sessions = sessions;
        }
    }
}
