package com.codestates.seb006main.message.entity;

import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String destinationId;
    @Enumerated(EnumType.STRING)
    private MessageType messageType;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Message(Long messageId, String body, MessageStatus messageStatus, String destinationId, MessageType messageType, Member member) {
        this.messageId = messageId;
        this.body = body;
        this.destinationId = destinationId;
        this.messageStatus = messageStatus == null ? MessageStatus.NOT_READ : messageStatus;
        this.messageType = messageType;
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

    public enum MessageType {
        POST, CHAT;

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
