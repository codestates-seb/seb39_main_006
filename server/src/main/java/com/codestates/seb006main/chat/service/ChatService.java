package com.codestates.seb006main.chat.service;

import com.codestates.seb006main.chat.dto.ChatDto;
import com.codestates.seb006main.chat.entity.Chat;
import com.codestates.seb006main.chat.repository.ChatRepository;
import com.codestates.seb006main.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public void saveChat(ChatDto.Message message) {
        Chat chat = Chat.builder()
                .roomId(message.getRoomId())
                .senderId(message.getSenderId())
                .message(message.getMessage())
                .build();

        chatRepository.save(chat);
    }

    public String getDisplayName(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow().getDisplayName();
    }
}
