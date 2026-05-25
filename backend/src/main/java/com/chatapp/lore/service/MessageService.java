package com.chatapp.lore.service;

import com.chatapp.lore.dto.MessageDto;
import com.chatapp.lore.model.ChatMessage;
import com.chatapp.lore.model.Room;
import com.chatapp.lore.model.User;
import com.chatapp.lore.repo.MessageRepo;
import com.chatapp.lore.repo.RoomRepo;
import com.chatapp.lore.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.chatapp.lore.dto.MessageResponseDto;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final UserRepo userRepo;
    private final RoomRepo roomRepo;
    private final MessageRepo messageRepo;

    // get all messages in a room
    @Transactional
    public List<MessageResponseDto> getMessageInRoom(String roomId) {

        roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        List<ChatMessage> messages =
                messageRepo.findByRoom_RoomIdOrderBySentAtAsc(roomId);

        return messages.stream()
                .map(message -> MessageResponseDto.builder()
                        .id(message.getId())
                        .content(message.getContent())
                        .senderId(message.getSender().getUserId()) // ✅ UUID
                        .sentAt(message.getSentAt())
                        .build())
                .toList();
    }

    // send message
    public MessageResponseDto sendMessage(MessageDto dto) {

        User sender = userRepo.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Room room = roomRepo.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!room.getUsers().contains(sender)) {
            throw new RuntimeException("User is not a member of this room");
        }

        ChatMessage message = ChatMessage.builder()
                .content(dto.getContent())
                .sender(sender)
                .room(room)
                .sentAt(LocalDateTime.now())
                .build();

        ChatMessage saved = messageRepo.save(message);

        return MessageResponseDto.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .senderId(saved.getSender().getUserId())
                .sentAt(saved.getSentAt())
                .build();
    }
}