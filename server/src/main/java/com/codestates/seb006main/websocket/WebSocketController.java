package com.codestates.seb006main.websocket;

import com.codestates.seb006main.config.DomainEvent;
import com.codestates.seb006main.dto.MultiResponseDto;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.message.dto.MessageDto;
import com.codestates.seb006main.message.service.MessageService;
import com.codestates.seb006main.posts.entity.MemberPosts;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class WebSocketController {
    private final MessageService messageService;
    private final WebSocketEventListener eventListener;
    private final SimpMessagingTemplate template;
    private final Gson gson;

    @GetMapping("/api/messages")
    public ResponseEntity getAllMessages(@PageableDefault(page = 1, sort = "messageId", direction = Sort.Direction.DESC) Pageable pageable,
                                         Authentication authentication) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = messageService.getAllMessages(authentication, pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/api/messages/read")
    public void readMessage(@RequestParam List<Long> messageId) {
        messageService.readAllMessages(messageId);
    }

    @GetMapping("/api/messages/not-read")
    public ResponseEntity getNotReadMessages(@PageableDefault(page = 1, sort = "messageId", direction = Sort.Direction.DESC) Pageable pageable,
                                             Authentication authentication) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber() - 1, pageable.getPageSize(), pageable.getSort());
        MultiResponseDto responseDto = messageService.sendNotSentMessage(authentication, pageRequest);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/api/messages/delete")
    public void deleteMessage(@RequestParam List<Long> messageId) {
        messageService.deleteAllMessages(messageId);
    }

    public void sendMessage(MessageDto.Response message) throws IOException {
        MemberSession session = eventListener.sessionMap.get(message.getEmail());
        if (session.sessionIds.isEmpty()) {
            messageService.failedToSend(message);
            return;
        }
        String content = gson.toJson(message);
        template.convertAndSend("/topic/" + session.getMemberId(), content);
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleMessagingListener(DomainEvent event) throws IOException {
        MessageDto.Response message = messageService.createMessage(event.getEntity(), event.getEventType());
        sendMessage(message);
    }

//    public void sendMatchingMessage(Matching matching, MessageDto.Response message) throws IOException {
//        MemberSession session = eventListener.sessionMap.get(matching.getPosts().getMember().getEmail());
//        if (session.sessionIds.isEmpty()) {
//            messageService.failedToSend(message);
//            return;
//        }
//        String content = gson.toJson(message);
//        template.convertAndSend("/topic/" + session.getMemberId(), content);
//    }
//
//    public void sendAcceptedMessage(MemberPosts memberPosts, MessageDto.Response message) throws IOException {
//        MemberSession session = eventListener.sessionMap.get(memberPosts.getMember().getEmail());
//        if (session.sessionIds.isEmpty()) {
//            messageService.failedToSend(message);
//            return;
//        }
//        String content = gson.toJson(message);
//        template.convertAndSend("/topic/" + session.getMemberId(), content);
//    }
    // TODO: 핸들링을 하나로 만들고 인스턴스 체크를 메시지 서비스에서 한다.
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void handleCreateMatching(DomainEvent event) throws IOException {
//        if (event.getEntity() instanceof Matching) {
//            MessageDto.Response message = messageService.createMessage(event.getEntity());
//            sendMatchingMessage((Matching) event.getEntity(), message);
//        }
//    }
//
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public void handleAcceptedMatching(DomainEvent event) throws IOException {
//        if (event.getEntity() instanceof MemberPosts) {
//            MessageDto.Response message = messageService.createMessage(event.getEntity());
//            sendAcceptedMessage((MemberPosts) event.getEntity(), message);
//        }
//    }
}
