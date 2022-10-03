package com.codestates.seb006main.websocket;

import com.codestates.seb006main.config.DomainEvent;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.message.dto.MessageDto;
import com.codestates.seb006main.message.service.MessageService;
import com.codestates.seb006main.posts.entity.MemberPosts;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public void readAllMessages(@RequestBody List<Long> messageIdList) {
        messageService.readAllMessages(messageIdList);
    }

    public void sendMatchingMessage(Matching matching, MessageDto.Response message) throws IOException {
        MemberSession session = eventListener.sessionMap.get(matching.getPosts().getMember().getEmail());
        if (session.sessionIds.isEmpty()) {
            messageService.failedToSend(message);
            return;
        }
        String content = gson.toJson(message);
        template.convertAndSend("/topic/" + session.getMemberId(), content);
    }

    public void sendAcceptedMessage(MemberPosts memberPosts, MessageDto.Response message) throws IOException {
        MemberSession session = eventListener.sessionMap.get(memberPosts.getMember().getEmail());
        if (session.sessionIds.isEmpty()) {
            messageService.failedToSend(message);
            return;
        }
        String content = gson.toJson(message);
        template.convertAndSend("/topic/" + session.getMemberId(), content);
    }
    // TODO: 핸들링을 하나로 만들고 인스턴스 체크를 메시지 서비스에서 한다.
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCreateMatching(DomainEvent event) throws IOException {
        if (event.getEntity() instanceof Matching) {
            MessageDto.Response message = messageService.createMessage(event.getEntity());
            sendMatchingMessage((Matching) event.getEntity(), message);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleAcceptedMatching(DomainEvent event) throws IOException {
        if (event.getEntity() instanceof MemberPosts) {
            MessageDto.Response message = messageService.createMessage(event.getEntity());
            sendAcceptedMessage((MemberPosts) event.getEntity(), message);
        }
    }
}
