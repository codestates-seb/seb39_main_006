package com.codestates.seb006main.message.entity;

import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketMessage;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private String body;
    @Enumerated(EnumType.STRING)
    private MessageStatus messageStatus;
    private Long postId;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Message(Long messageId, String body, MessageStatus messageStatus, Long postId, Member member) {
        this.messageId = messageId;
        this.body = body;
        this.postId = postId;
        this.messageStatus = messageStatus == null ? MessageStatus.NOT_READ : messageStatus;
        this.member = member;
    }

    public enum MessageStatus {
        NOT_SENT(0, "미발송"),
        NOT_READ(1, "읽지 않음"),
        READ(2, "읽음");

        int stepNumber;
        String messageDescription;

        MessageStatus(int stepNumber, String messageDescription) {
            this.stepNumber = stepNumber;
            this.messageDescription = messageDescription;
        }
    }

    public void failedToSend() {
        this.messageStatus = MessageStatus.NOT_SENT;
    }

    public void send() {
        this.messageStatus = MessageStatus.NOT_READ;
    }

    public void read() {
        this.messageStatus = MessageStatus.READ;
    }
}
