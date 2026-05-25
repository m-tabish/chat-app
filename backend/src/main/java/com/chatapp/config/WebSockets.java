package com.chatapp.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSockets implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // connection establishes on /chat endpoint from client\
        // i.e. /chat will be hit by the client to send the messages
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){

        // server sends all messages to /app on client side
        config.setApplicationDestinationPrefixes("/app");

        // all messages received by the server on /app will be broadcasted to /topic on the client side to which the users are subscribed to
        config.enableSimpleBroker("/topic");

    }


}
