package com.chatapp.lore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageResponseDto {
    private String id;
    private String content;
    private String senderId;
    private LocalDateTime sentAt;
}
