package com.example.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket + STOMP 설정
 *
 * 클라이언트 → ws://localhost:8080/ws 로 연결
 * 서버 → /topic/** 을 구독한 모든 클라이언트에게 메시지 전송 (메모리 브로커, DB 불필요)
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 브라우저가 WebSocket 연결을 맺는 주소
        registry.addEndpoint("/ws");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 서버 → 클라이언트 : /topic 으로 시작하는 주소를 구독한 사람에게 전달
        registry.enableSimpleBroker("/topic");
        // 클라이언트 → 서버 : /app 으로 시작하는 주소는 @MessageMapping 메서드로 전달
        registry.setApplicationDestinationPrefixes("/app");
    }
}
