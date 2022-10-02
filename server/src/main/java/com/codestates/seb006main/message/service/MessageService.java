package com.codestates.seb006main.message.service;

import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.message.dto.MessageDto;
import com.codestates.seb006main.message.entity.Message;
import com.codestates.seb006main.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    public MessageDto.Response sendMessage(Matching matching) {
        String body = matching.getMember().getDisplayName() + "님이 \"" + matching.getPosts().getTitle() + "\" 글에 매칭 요청을 보내셨습니다.";
        Message message = Message.builder()
                .body(body)
                .postId(matching.getPosts().getPostId())
                .member(matching.getPosts().getMember())
                .build();
        messageRepository.save(message);

        // TODO: 리팩토링 이쁘게.
        return MessageDto.Response.builder()
                .message(message)
                .build();
    }

    public void failedToSend(MessageDto.Response messageDto) {
        Message message = messageRepository.findById(messageDto.getMessageId()).orElseThrow();
        message.failedToSend();
        messageRepository.save(message);
    }

    public void readAllMessages(List<Long> messageIdList) {
        for (Long messageId : messageIdList) {
            Message message = messageRepository.findById(messageId).orElseThrow();
            message.read();
            messageRepository.save(message);
        }
    }
}
