package com.codestates.seb006main.message.service;

import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.message.dto.MessageDto;
import com.codestates.seb006main.message.entity.Message;
import com.codestates.seb006main.message.repository.MessageRepository;
import com.codestates.seb006main.posts.entity.MemberPosts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageDto.Response createMessage(Object entity) {
        String body = null;
        Long postId = null;
        Member member = null;

        if (entity instanceof Matching) {
            Matching matching = (Matching) entity;
            body = "[" + matching.getMember().getDisplayName() + "] 님이 [" + matching.getPosts().getTitle() + "] 글에 매칭 요청을 보내셨습니다.";
            postId = matching.getPosts().getPostId();
            member = matching.getPosts().getMember();
        } else if (entity instanceof MemberPosts) {
            MemberPosts memberPosts = (MemberPosts) entity;
            body = "[" + memberPosts.getPosts().getTitle() + "] 게시글에 대한 매칭 요청이 수락되었습니다.";
            postId = memberPosts.getPosts().getPostId();
            member = memberPosts.getMember();
        } else {
            throw new BusinessLogicException(ExceptionCode.LACK_OF_INFORMATION);
        }

        Message message = Message.builder()
                .body(body)
                .postId(postId)
                .member(member)
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
