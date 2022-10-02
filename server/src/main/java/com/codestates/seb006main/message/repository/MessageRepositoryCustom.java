package com.codestates.seb006main.message.repository;

import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.message.entity.Message;

import java.util.List;

public interface MessageRepositoryCustom {
    List<Message> findNotSentMessages(String email);
}
