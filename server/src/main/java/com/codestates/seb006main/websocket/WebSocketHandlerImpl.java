package com.codestates.seb006main.websocket;

import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.message.dto.MessageDto;
import com.codestates.seb006main.message.entity.Message;
import com.codestates.seb006main.message.repository.MessageRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class WebSocketHandlerImpl implements WebSocketHandler {
    private final MessageRepository messageRepository;
    private final Gson gson;
    Map<String, WebSocketSession> userSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (userSessionMap.containsKey(session.getPrincipal().getName())) {
            return;
        }
        userSessionMap.put(session.getPrincipal().getName(), session);
        List<Message> notSentMessages = messageRepository.findNotSentMessages(session.getPrincipal().getName());
        if (!notSentMessages.isEmpty()) {
            for (Message message : notSentMessages) {
                MessageDto.Response response = MessageDto.Response.builder().message(message).build();
                String content = gson.toJson(response);
                session.sendMessage(new TextMessage(content));
            }
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        if (!userSessionMap.containsKey(session.getPrincipal().getName())) {
            return;
        }
        userSessionMap.remove(session.getPrincipal().getName());
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
