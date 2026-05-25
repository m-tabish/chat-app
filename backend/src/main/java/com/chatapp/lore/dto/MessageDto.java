package com.chatapp.lore.dto;


import com.chatapp.lore.model.MessageType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDto {
    private String content;
    private String senderId;
    private MessageType messageType;
    private String roomId;
}
