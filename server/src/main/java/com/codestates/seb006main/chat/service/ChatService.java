package com.codestates.seb006main.chat.service;

import com.codestates.seb006main.chat.dto.ChatDto;
import com.codestates.seb006main.chat.entity.Chat;
import com.codestates.seb006main.chat.repository.ChatRepository;
import com.codestates.seb006main.jwt.JwtUtils;
import com.codestates.seb006main.members.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    public Long saveAndSendChatWithSenderId(ChatDto.Message message, String accessToken) {
        Long senderId = getMemberIdFromToken(accessToken);
        Chat chat = Chat.builder()
                .roomId(message.getRoomId())
                .senderId(senderId)
                .message(message.getMessage())
                .build();
        chatRepository.save(chat);
        return senderId;
    }

    public String getDisplayName(String accessToken) {
        accessToken = accessToken.replace("Bearer ", "");
        return memberRepository.findById(getMemberIdFromToken(accessToken)).orElseThrow().getDisplayName();
    }

    public Long getMemberIdFromToken(String accessToken) {
        accessToken = accessToken.replace("Bearer ", "");
        System.out.println(jwtUtils.getMemberIdFromToken(accessToken));
        return jwtUtils.getMemberIdFromToken(accessToken);
    }
}
