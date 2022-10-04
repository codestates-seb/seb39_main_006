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
    public static class Response { // implements WebSocketMessage
        private Long messageId;
        private String body;
        private String email;
        private Long postId;
        private Message.MessageStatus messageStatus;

        @Builder
        public Response(Message message) {
            this.messageId = message.getMessageId();
            this.body = message.getBody();
            this.email = message.getMember().getEmail();
            this.postId = message.getPostId();
            this.messageStatus = message.getMessageStatus();
        }
    }
}
