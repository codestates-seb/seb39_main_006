package com.codestates.seb006main.config;

import com.codestates.seb006main.members.entity.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationEvent;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
        CANCEL_MATCHING;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Domain{
        Long postId;
        String title;
        Member sender;
        Member receiver;

        public Domain(Long postId, String title, Member sender, Member receiver) {
            this.postId = postId;
            this.title = title;
            this.sender = sender;
            this.receiver = receiver;
        }
    }
}
