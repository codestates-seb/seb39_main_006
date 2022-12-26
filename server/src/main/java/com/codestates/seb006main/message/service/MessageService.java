package com.codestates.seb006main.message.service;

import com.codestates.seb006main.auth.PrincipalDetails;
import com.codestates.seb006main.util.DomainEvent;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageDto.Response createMessage(Object entity, DomainEvent.EventType eventType) {
        String body;
        String destinationId;
        Member member;
        Message.MessageType messageType;
        DomainEvent.PostEvent postEvent;
        DomainEvent.ChatEvent chatEvent;

//        if (eventType == DomainEvent.EventType.CREATE_MATCHING) {
//            Matching matching = (Matching) entity;
//            body = "[" + matching.getMember().getDisplayName() + "] 님이 [" + matching.getPosts().getTitle() + "] 여행에 매칭 요청을 보내셨습니다.";
//            destinationId = String.valueOf(matching.getPosts().getPostId());
//            member = matching.getPosts().getMember();
//        } else if (eventType == DomainEvent.EventType.APPLY_MATCHING) {
//            MemberPosts memberPosts = (MemberPosts) entity;
//            body = "[" + memberPosts.getPosts().getTitle() + "] 여행에 대한 매칭 요청이 수락되었습니다.";
//            destinationId = String.valueOf(memberPosts.getPosts().getPostId());
//            member = memberPosts.getMember();
//        } else if (eventType == DomainEvent.EventType.CANCEL_PARTICIPATION) {
//            DomainEvent.PostEvent postEvent = (DomainEvent.PostEvent) entity;
//            body = "[" + postEvent.getTitle() + "] 여행 참여가 취소되었습니다.";
//            destinationId = String.valueOf(postEvent.getPostId());
//            member = postEvent.getReceiver();
//        } else if (eventType == DomainEvent.EventType.CANCEL_MATCHING) {
//            DomainEvent.PostEvent postEvent = (DomainEvent.PostEvent) entity;
//            body = "[" + postEvent.getTitle() + "] 여행에 대한 [" + postEvent.getSender().getDisplayName() + "]의 매칭 요청이 취소되었습니다.";
//            destinationId = String.valueOf(postEvent.getPostId());
//            member = postEvent.getReceiver();
//        } else if (eventType == DomainEvent.EventType.CREATE_ROOM) {
//            // TODO: ID 타입에 대한 문제. 분리해야할까?
//            DomainEvent.ChatEvent chatEvent = (DomainEvent.ChatEvent) entity;
//            body = "[" + chatEvent.getSender().getDisplayName() + "님이 대화 요청을 보내셨습니다.";
//            destinationId = chatEvent.getRoomId();
//            member = chatEvent.getReceiver();
//        } else {
//            throw new BusinessLogicException(ExceptionCode.LACK_OF_INFORMATION);
//        }
        switch (eventType) {
            case CREATE_MATCHING:
                Matching matching = (Matching) entity;
                body = "[" + matching.getMember().getDisplayName() + "] 님이 [" + matching.getPosts().getTitle() + "] 여행에 매칭 요청을 보내셨습니다.";
                destinationId = String.valueOf(matching.getPosts().getPostId());
                member = matching.getPosts().getMember();
                messageType = Message.MessageType.POST;
                break;
            case APPLY_MATCHING:
                MemberPosts memberPosts = (MemberPosts) entity;
                body = "[" + memberPosts.getPosts().getTitle() + "] 여행에 대한 매칭 요청이 수락되었습니다.";
                destinationId = String.valueOf(memberPosts.getPosts().getPostId());
                member = memberPosts.getMember();
                messageType = Message.MessageType.POST;
                break;
            case CANCEL_MATCHING:
                postEvent = (DomainEvent.PostEvent) entity;
                body = "[" + postEvent.getTitle() + "] 여행에 대한 [" + postEvent.getSender().getDisplayName() + "] 의 매칭 요청이 취소되었습니다.";
                destinationId = String.valueOf(postEvent.getPostId());
                member = postEvent.getReceiver();
                messageType = Message.MessageType.POST;
                break;
            case CANCEL_PARTICIPATION:
                postEvent = (DomainEvent.PostEvent) entity;
                body = "[" + postEvent.getTitle() + "] 여행 참여가 취소되었습니다.";
                destinationId = String.valueOf(postEvent.getPostId());
                member = postEvent.getReceiver();
                messageType = Message.MessageType.POST;
                break;
            case CREATE_ROOM:
                // TODO: ID 타입에 대한 문제. 분리해야할까?
                chatEvent = (DomainEvent.ChatEvent) entity;
                body = "[" + chatEvent.getSender().getDisplayName() + "] 님이 대화 요청을 보내셨습니다.";
                destinationId = chatEvent.getRoomId();
                member = chatEvent.getReceiver();
                messageType = Message.MessageType.CHAT;
                break;
            default:
                throw new BusinessLogicException(ExceptionCode.LACK_OF_INFORMATION);
        }

        Message message = Message.builder()
                .body(body)
                .destinationId(destinationId)
                .member(member)
                .messageType(messageType)
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

    public MultiResponseDto sendNotSentMessage(Authentication authentication, PageRequest pageRequest) {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        List<Message> notSentMessagesList = messageRepository.findNotSentMessages(principalDetails.getUsername());
        if (!notSentMessagesList.isEmpty()) {
            for (Message notSentMessage : notSentMessagesList) {
                notSentMessage.send();
                messageRepository.save(notSentMessage);
            }
        }
        Page<Message> notSentMessagesPage = new PageImpl<>(notSentMessagesList, pageRequest, notSentMessagesList.size());
        return new MultiResponseDto<>(messageMapper.messageListToResponseDtoList(notSentMessagesList), notSentMessagesPage);
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
        }
    }

    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.MESSAGE_NOT_FOUND));
        if (message.getMessageStatus() == Message.MessageStatus.READ) {
            messageRepository.deleteById(messageId);
        }
    }

    public void deleteAllMessages(List<Long> messageIdList) {
        if (!messageIdList.isEmpty()) {
            for (Long messageId : messageIdList) {
                deleteMessage(messageId);
            }
        }
    }
}
