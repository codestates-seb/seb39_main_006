package com.codestates.seb006main.chat.dto;

import lombok.*;

public class ChatDto {
    @Getter
    @Setter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Message {
        private String roomId;
        private Long senderId;
        private String message;

        @Builder
        public Message(String roomId,Long senderId, String message) {
            this.roomId = roomId;
            this.senderId = senderId;
            this.message = message;
        }
    }
}
