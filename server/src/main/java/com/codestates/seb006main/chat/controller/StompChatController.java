package com.codestates.seb006main.chat.controller;

import com.codestates.seb006main.chat.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StompChatController {
    private final SimpMessagingTemplate template;

    @MessageMapping("/chat/enter")
    public void enter(ChatDto.Message message) {
        message.setMessage(message.getWriter() + "님이 채팅방에 참여하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/message")
    public void message(ChatDto.Message message) {
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    @MessageMapping("/chat/exit")
    public void exit(ChatDto.Message message) {
        message.setMessage(message.getWriter() + "님이 채팅방에서 퇴장하셨습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}
