package com.codestates.seb006main.websocket;

import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.jwt.JwtUtils;
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
import java.util.Map;

@RequiredArgsConstructor
@Component
public class WebSocketEventListener {
    private final SimpMessagingTemplate template;
    private final JwtUtils jwtUtils;
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
        System.out.println("Received a new web socket connection. Session ID : " + accessor.getSessionId());
    }

    @EventListener
    public void handleSub(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            Long memberId = null;
            for(Map.Entry<String, MemberSession> entry : sessionMap.entrySet()) {
                if (entry.getValue().getSessionIds().contains(accessor.getSessionId())) {
                    memberId = entry.getValue().getMemberId();
                }
            }
            if (memberId == null) {
                throw new BusinessLogicException(ExceptionCode.SESSION_NOT_FOUND);
            }
            template.convertAndSend("/topic/" + memberId, "현재 세션 ID " + accessor.getSessionId() + "에서 구독 완료하였습니다.");
        }
    }

    @EventListener
    public void handleWebSocketDisConnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        for(Map.Entry<String, MemberSession> entry : sessionMap.entrySet()) {
            entry.getValue().getSessionIds().remove(accessor.getSessionId());
        }
        System.out.println("Web socket session closed. Message : " + event.getMessage());
    }
}
