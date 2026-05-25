package com.chatapp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data                // Generates Getters, Setters, toString, etc.
@NoArgsConstructor   // Required for JSON deserialization
@AllArgsConstructor

public class RoomDto {
    private String roomName;
    private String adminId;
}
