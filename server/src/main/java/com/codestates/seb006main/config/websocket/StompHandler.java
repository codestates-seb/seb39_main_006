package com.codestates.seb006main.config.websocket;

import com.codestates.seb006main.message.dto.MessageDto;
import com.codestates.seb006main.message.repository.MessageRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor, WebSocketHandler {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        System.out.println("message:" + message);
        System.out.println("헤더 : " + message.getHeaders());
        System.out.println("토큰" + accessor.getNativeHeader("access_hh"));
        System.out.println(accessor.getCommand());
        System.out.println(accessor.getMessage());
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            System.out.println(accessor.getMessageHeaders());
        }
        return message;
    }



    private final MessageRepository messageRepository;
    private final Gson gson;
    Map<String, WebSocketSession> userSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (userSessionMap.containsKey(session.getPrincipal().getName())) {
            return;
        }
        System.out.println("혹시 여기 오나요?");
        userSessionMap.put(session.getPrincipal().getName(), session);
        List<com.codestates.seb006main.message.entity.Message> notSentMessages = messageRepository.findNotSentMessages(session.getPrincipal().getName());
        if (!notSentMessages.isEmpty()) {
            for (com.codestates.seb006main.message.entity.Message message : notSentMessages) {
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
