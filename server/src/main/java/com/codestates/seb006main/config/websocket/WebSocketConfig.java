package com.codestates.seb006main.config.websocket;

import com.codestates.seb006main.websocket.WebSocketHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {
    private final WebSocketHandlerImpl webSocketHandler;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler, "/websocket").setAllowedOrigins("http://localhost:3000").setAllowedOriginPatterns("*");
        registry.addHandler(webSocketHandler, "/websocket").setAllowedOrigins("http://localhost:3000").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("http://localhost:3000").setAllowedOriginPatterns("*");
        registry.addEndpoint("/websocket").setAllowedOrigins("http://localhost:3000").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                System.out.println(accessor.getCommand());
                System.out.println(accessor.getMessage());
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    System.out.println(accessor.getMessageHeaders());
                }
                return message;
            }
        });
    }
}
