package com.codestates.seb006main.config;

import lombok.Getter;
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
        CANCEL_PARTICIPATION;
    }
}
