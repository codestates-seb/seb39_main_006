package com.codestates.seb006main.message.repository;

import com.codestates.seb006main.message.entity.Message;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.codestates.seb006main.message.entity.QMessage.message;

public class MessageRepositoryImpl implements MessageRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public MessageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Message> findNotSentMessages(String email) {
        return queryFactory
                .selectFrom(message)
                .where(message.member.email.eq(email)
                        .and(message.messageStatus.ne(Message.MessageStatus.READ)))
                .fetch();
    }
}
