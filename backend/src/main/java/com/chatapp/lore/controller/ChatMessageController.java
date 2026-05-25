package com.chatapp.lore.controller;

import com.chatapp.lore.dto.MessageDto;
import com.chatapp.lore.dto.MessageResponseDto;
import com.chatapp.lore.service.MessageService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RequiredArgsConstructor
@Controller
@RestController
@RequestMapping("/message")
public class ChatMessageController {


    private final MessageService messageService;

    @Transactional
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getAllMessageInRoom(@PathVariable String roomId) {
        List<MessageResponseDto> messageList = messageService.getMessageInRoom(roomId).stream()
                .map(m -> new MessageResponseDto(
                        m.getId(),
                        m.getContent(),
                        m.getSenderId(),
                        m.getSentAt()
                )).toList();
        return ResponseEntity.ok(messageList);
    }

    @PostMapping("/send")
    @Transactional
    public ResponseEntity<?> sendMessage(@RequestBody MessageDto dto) {
        return ResponseEntity.ok(messageService.sendMessage(dto));

    }
}
