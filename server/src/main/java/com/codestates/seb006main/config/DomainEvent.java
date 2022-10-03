package com.codestates.seb006main.config;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class DomainEvent extends ApplicationEvent {
    private Object entity;
    public DomainEvent(Object source, Object entity) {
        super(source);
        this.entity = entity;
    }
}
