package com.codestates.seb006main.util;

import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

@Getter
public class DomainEvent extends ApplicationEvent {
    private Object entity;
    private EventType eventType;
    public DomainEvent(Object source, Object entity, EventType eventType) {
        super(source);
        this.entity = entity;
        this.eventType = eventType;
    }

    public enum EventType {
        CREATE_MATCHING,
        APPLY_MATCHING,
        CANCEL_PARTICIPATION,
        CANCEL_MATCHING,
        CREATE_ROOM;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class PostEvent {
        Long postId;
        String title;
        Member sender;
        Member receiver;

        public PostEvent(Long postId, String title, Member sender, Member receiver) {
            this.postId = postId;
            this.title = title;
            this.sender = sender;
            this.receiver = receiver;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChatEvent{
        String roomId;
        String title;
        Member sender;
        Member receiver;

        public ChatEvent(String roomId, String title, Member sender, Member receiver) {
            this.roomId = roomId;
            this.title = title;
            this.sender = sender;
            this.receiver = receiver;
        }
    }
}
