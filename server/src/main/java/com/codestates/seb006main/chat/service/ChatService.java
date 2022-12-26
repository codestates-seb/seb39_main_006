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

    //TODO: 이 부분을 카프카에 메시지를 보낸 뒤 처리하도록 한다.
    public void saveChat(ChatDto.Message message) {
        Chat chat = Chat.builder()
                .roomId(message.getRoomId())
                .senderId(message.getSenderId())
                .message(message.getMessage())
                .build();
        chatRepository.save(chat);
    }

    public String getDisplayName(String accessToken) {
        accessToken = accessToken.replace("Bearer ", "");
        return memberRepository.findById(getMemberIdFromToken(accessToken)).orElseThrow().getDisplayName();
    }

    public Long getMemberIdFromToken(String accessToken) {
        accessToken = accessToken.replace("Bearer ", "");
        return jwtUtils.getMemberIdFromToken(accessToken);
    }
}
