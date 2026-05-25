package com.chatapp.lore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomRequestDto {
    private String roomId;

    private String userId;
}
