package com.codestates.seb006main.websocket;

import com.codestates.seb006main.config.DomainEvent;
import com.codestates.seb006main.matching.entity.Matching;
import com.codestates.seb006main.message.service.MessageService;
import com.codestates.seb006main.message.dto.MessageDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class WebSocketController {
    private final MessageService messageService;
    private final WebSocketHandlerImpl webSocketHandler;
    private final Gson gson;

    @GetMapping("/api/messages")
    public void readAllMessages(@RequestBody List<Long> messageIdList) {
        messageService.readAllMessages(messageIdList);
    }

    public void sendMessage(Matching matching, MessageDto.Response message) throws IOException {
        WebSocketSession session = webSocketHandler.userSessionMap.get(matching.getPosts().getMember().getEmail());
        if (session == null) {
            messageService.failedToSend(message);
            return;
        }
        String content = gson.toJson(message);
        session.sendMessage(new TextMessage(content));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleCreateMatching(DomainEvent event) throws IOException {
        if (event.getEntity() instanceof Matching) {
            MessageDto.Response message = messageService.sendMessage((Matching) event.getEntity());
            sendMessage((Matching) event.getEntity(), message);
        }
    }
}
