package com.codestates.seb006main.websocket;

import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.jwt.JwtUtils;
import com.codestates.seb006main.message.dto.MessageDto;
import com.codestates.seb006main.message.entity.Message;
import com.codestates.seb006main.message.repository.MessageRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {
    private final SimpMessagingTemplate template;
    private final JwtUtils jwtUtils;
    private final Gson gson;
    private final MessageRepository messageRepository;
    Map<String, MemberSession> sessionMap = new HashMap<>();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        GenericMessage generic = (GenericMessage) accessor.getHeader("simpConnectMessage");
        Map nativeHeaders = (Map) generic.getHeaders().get("nativeHeaders");
        ArrayList access_hh = (ArrayList) nativeHeaders.get("access_hh");
        String accessToken = (String) access_hh.get(0);
        accessToken = accessToken.replace("Bearer ", "");
        Map<String, Object> map = jwtUtils.getClaimsFromToken(accessToken, "access");
        MemberSession session = new MemberSession((Long) map.get("id"), accessor.getSessionId());
        sessionMap.put((String) map.get("email"), session);
        System.out.println("*******************************************************************************");
        System.out.println(map.get("email") + ": Received a new web socket connection. Session ID : " + accessor.getSessionId());
        System.out.println("*******************************************************************************");
    }

    @EventListener
    public void handleSub(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String email = null;
            Long memberId = null;
            for(Map.Entry<String, MemberSession> entry : sessionMap.entrySet()) {
                if (entry.getValue().getSessionIds().contains(accessor.getSessionId())) {
                    memberId = entry.getValue().getMemberId();
                    email = entry.getKey();
                }
            }
            if (memberId == null || email == null) {
                throw new BusinessLogicException(ExceptionCode.SESSION_NOT_FOUND);
            }
//            template.convertAndSend("/topic/" + memberId, "현재 세션 ID " + accessor.getSessionId() + "에서 구독 완료하였습니다.");
            System.out.println("*******************************************************************************");
            System.out.println(email + ": Now Subscribed /topic/" + memberId + ". Session ID : " + accessor.getSessionId());
            System.out.println("*******************************************************************************");
//            sendNotSentMessage(memberId, email);
        }
    }

    private void sendNotSentMessage(Long memberId, String email) {
        List<Message> notSentMessages = messageRepository.findNotSentMessages(email);
        if (!notSentMessages.isEmpty()) {
            for (Message notSentMessage : notSentMessages) {
                MessageDto.Response message = MessageDto.Response.builder().message(notSentMessage).build();
                String content = gson.toJson(message);
                notSentMessage.send();
                messageRepository.save(notSentMessage);
                template.convertAndSend("/topic/" + memberId, content);
            }
        }
    }

    @EventListener
    public void handleWebSocketDisConnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String email = null;
        for(Map.Entry<String, MemberSession> entry : sessionMap.entrySet()) {
            if (entry.getValue().getSessionIds().contains(accessor.getSessionId())) {
                entry.getValue().getSessionIds().remove(accessor.getSessionId());
                email = entry.getKey();
                // TODO: 모든 세션 아이디가 비어져있을 때 키값을 지우는게 좋을까? -> 오랫동안 접속하지 않을 경우 서버 세션에 남는 것 방지.
//                if (entry.getValue().sessionIds.isEmpty()) {
//                    sessionMap.remove(email);
//                }
            }
        }
        System.out.println("*******************************************************************************");
        System.out.println(email + ": Web socket session closed. Message : " + event.getMessage());
        System.out.println("*******************************************************************************");

    }
}
