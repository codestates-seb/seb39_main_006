package com.codestates.seb006main.message.service;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.members.entity.Member;
import com.codestates.seb006main.message.dto.MessageDto;
import com.codestates.seb006main.message.entity.Message;
import com.codestates.seb006main.message.mapper.MessageMapper;
import com.codestates.seb006main.message.repository.MessageRepository;
import com.codestates.seb006main.posts.entity.MemberPosts;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageDto.Response createMessage(Object entity) {
        String body;
        Long postId;
        Member member;

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

        return messageMapper.messageToResponseDto(message);
    }

    public void failedToSend(MessageDto.Response messageDto) {
        Message message = messageRepository.findById(messageDto.getMessageId()).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MESSAGE_NOT_FOUND));
        message.failedToSend();
        messageRepository.save(message);
    }

    public MultiResponseDto getAllMessages(Authentication authentication, PageRequest pageRequest) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Page<Message> messagePage = messageRepository.findByMember(principalDetails.getMember(), pageRequest);
        List<Message> messageList = messagePage.getContent();
        return new MultiResponseDto<>(messageMapper.messageListToResponseDtoList(messageList), messagePage);
    }

    public void readMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MESSAGE_NOT_FOUND));
        message.read();
        messageRepository.save(message);
    }

    public void readAllMessages(List<Long> messageIdList) {
        if (!messageIdList.isEmpty()) {
            for (Long messageId : messageIdList) {
                readMessage(messageId);
            }
        } else {
            throw new BusinessLogicException(ExceptionCode.MESSAGE_NOT_FOUND);
        }
    }
}
