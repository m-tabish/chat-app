package com.chatapp.lore.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chatapp.lore.dto.MessageDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatWSController {


    private final SimpMessagingTemplate messagingTemplate;

    // new user joined the chat

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(MessageDto dto) {
        // Broadcast to the room topic
        messagingTemplate.convertAndSend("/topic/room/" + dto.getRoomId(), dto);
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    @Transactional
    public MessageDto addUser(@Payload MessageDto dto, SimpMessageHeaderAccessor headerAccessor){

        // Add username in web socker session
        var sessionAttributes = headerAccessor.getSessionAttributes();
        if (sessionAttributes != null) {
            sessionAttributes.put("username", dto.getSenderId());
        }
        return dto;
    }

}
