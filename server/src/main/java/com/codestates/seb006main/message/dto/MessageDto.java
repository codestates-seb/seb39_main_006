package com.codestates.seb006main.message.dto;

import com.codestates.seb006main.message.entity.Message;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketMessage;

public class MessageDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Response implements WebSocketMessage {
        private Long messageId;
        private String body;
        private Long postId;
        private Message.MessageStatus messageStatus;

        @Builder
        public Response(Message message) {
            this.messageId = message.getMessageId();
            this.body = message.getBody();
            this.postId = message.getPostId();
            this.messageStatus = message.getMessageStatus();
        }

        @Override
        public Object getPayload() {
            return this;
        }

        @Override
        public int getPayloadLength() {
            return this.body.length();
        }

        @Override
        public boolean isLast() {
            return false;
        }
    }
}
