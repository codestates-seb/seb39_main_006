package com.codestates.seb006main.chat.controller;

import com.codestates.seb006main.chat.dto.ChatDto;
import com.codestates.seb006main.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.OnMessage;

@RestController
@RequiredArgsConstructor
public class StompChatController {
    private final SimpMessagingTemplate template;
    private final ChatService chatService;
    private final KafkaTemplate<String, ChatDto.Message> kafkaTemplate;
    @Value("${spring.kafka.topic.name}")
    private String KAFKA_TOPIC;

    // TODO: StompHandler에서 토큰 값을 가져온 뒤에 세션 정보를 저장하던가 혹은 웹소켓 연결 중에 유저 정보를 저장해놓을 수 있는 방법 강구
    @MessageMapping("/chat/enter")
    public void enterRoom(ChatDto.Message message, @Header("access_hh") String accessToken) {
        message.setSenderId(chatService.getMemberIdFromToken(accessToken));
        message.setMessage(chatService.getDisplayName(accessToken) + "님이 채팅방에 참여하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @OnMessage
    @MessageMapping("/chat/message")
    public void sendMessage(ChatDto.Message message, @Header("access_hh") String accessToken) {
        message.setSenderId(chatService.getMemberIdFromToken(accessToken));
        kafkaTemplate.send(KAFKA_TOPIC, message);
    }

    // Kafka로 보낸 뒤 저장하기 (메시지 순서 보장 및 손실 방지)
    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.topic.name}")
    public void receiveMessage(ChatDto.Message message) {
        chatService.saveChat(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/exit")
    public void exitRoom(ChatDto.Message message, @Header("access_hh") String accessToken) {
        message.setSenderId(chatService.getMemberIdFromToken(accessToken));
        message.setMessage(chatService.getDisplayName(accessToken) + "님이 채팅방에서 퇴장하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
