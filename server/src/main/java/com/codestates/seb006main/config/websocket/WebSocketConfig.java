package com.codestates.seb006main.config.websocket;

import com.codestates.seb006main.websocket.WebSocketHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
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
        registry.addHandler(webSocketHandler, "/websocket").setAllowedOrigins("*").setAllowedOriginPatterns("*");
        registry.addHandler(webSocketHandler, "/websocket").setAllowedOrigins("*").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket").setAllowedOrigins("*").setAllowedOriginPatterns("*");
        registry.addEndpoint("/websocket").setAllowedOrigins("*").setAllowedOriginPatterns("*").withSockJS();
    }
}
