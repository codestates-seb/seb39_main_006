package com.codestates.seb006main.config.websocket;

import com.codestates.seb006main.exception.BusinessLogicException;
import com.codestates.seb006main.exception.ExceptionCode;
import com.codestates.seb006main.jwt.JwtUtils;
import com.codestates.seb006main.util.MemberSession;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final JwtUtils jwtUtils;
    public final Map<String, MemberSession> sessionMap = new HashMap<>();
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Map<String, Object> tokenMap;

        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String accessToken = accessor.getFirstNativeHeader("access_hh");
            assert accessToken != null;
            accessToken = accessToken.replace("Bearer ", "");
            tokenMap = jwtUtils.getClaimsFromToken(accessToken, "access");
            MemberSession memberSession = new MemberSession((Long) tokenMap.get("id"), accessor.getSessionId());
            sessionMap.put((String) tokenMap.get("email"), memberSession);

        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            String email = null;
            Long memberId = null;
            for (Map.Entry<String, MemberSession> entry : sessionMap.entrySet()) {
                if (entry.getValue().getSessionIds().contains(accessor.getSessionId())) {
                    memberId = entry.getValue().getMemberId();
                    email = entry.getKey();
                }
            }
            if (memberId == null || email == null) {
                throw new BusinessLogicException(ExceptionCode.SESSION_NOT_FOUND);
            }

           if (Objects.requireNonNull(accessor.getDestination()).startsWith("/sub")) {
               MemberSession memberSession = sessionMap.get(email);
               memberSession.setLastConnected(LocalDateTime.now());
           }

        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
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
        return message;
    }
}
