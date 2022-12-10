package com.codestates.seb006main.config.websocket;

import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.jwt.JwtUtils;
import com.codestates.seb006main.util.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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
public class MessageEventListener {
    private final JwtUtils jwtUtils;
    public final Map<String, MemberSession> sessionMap = new HashMap<>();

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
    }
}
