package com.chatapp.config;

import com.chatapp.dto.MessageDto;
import com.chatapp.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.StreamingHttpOutputMessage;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Controller
@Slf4j
@RequiredArgsConstructor
public class WebSockerEventListener {
    // each time user disconnects , event is created
    @Autowired
    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event ){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username != null){
            log.info("User disconnected {}", username);
            try {
                var chatMessage = MessageDto.builder()
                        .messageType(MessageType.LEAVE )
                        .senderId(username)
                        .build();

                messageTemplate.convertAndSend("/topic/public", chatMessage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

