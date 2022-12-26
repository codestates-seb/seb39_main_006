package com.codestates.seb006main.message.dto;

import com.codestates.seb006main.message.entity.Message;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MessageDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response { // implements WebSocketMessage
        private Long messageId;
        private String body;
        private String email;
        private String destinationId;
        private Message.MessageStatus messageStatus;
        private Message.MessageType messageType;

        @Builder
        public Response(Message message) {
            this.messageId = message.getMessageId();
            this.body = message.getBody();
            this.email = message.getMember().getEmail();
            this.destinationId = message.getDestinationId();
            this.messageStatus = message.getMessageStatus();
            this.messageType = message.getMessageType();
        }
    }
}
