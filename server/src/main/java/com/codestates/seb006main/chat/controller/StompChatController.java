package com.codestates.seb006main.chat.controller;

import com.codestates.seb006main.chat.dto.ChatDto;
import com.codestates.seb006main.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.OnMessage;

@RestController
@RequiredArgsConstructor
public class StompChatController {
    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    // TODO: StompHandler에서 토큰 값을 가져온 뒤에 세션 정보를 저장하던가 혹은 웹소켓 연결 중에 유저 정보를 저장해놓을 수 있는 방법 강구
    @MessageMapping("/chat/enter")
    public void enter(ChatDto.Message message) {
        String displayName = chatService.getDisplayName(message.getSenderId());
        message.setMessage(displayName + "님이 채팅방에 참여하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }


    @OnMessage
    @MessageMapping("/chat/message")
    public void message(ChatDto.Message message) {
        chatService.saveChat(message);
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/exit")
    public void exit(ChatDto.Message message) {
        String displayName = chatService.getDisplayName(message.getSenderId());
        message.setMessage(displayName + "님이 채팅방에서 퇴장하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
