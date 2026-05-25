package com.chatapp.controller;


import com.chatapp.dto.JoinRoomRequestDto;
import com.chatapp.dto.RoomDto;
import com.chatapp.model.Room;
import com.chatapp.repo.RoomRepo;
import com.chatapp.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/room" )
@RequiredArgsConstructor // Lombok creates constructor for all final fields
public class RoomController {

    private final RoomRepo roomRepo; // 'final' is key for constructor injection
    private final RoomService roomService;

    // Get all Rooms
    @GetMapping({"", "/"})
    public ResponseEntity<?> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }

    // Get room info
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable String roomId) {

        Room room = roomRepo.findByRoomId(roomId);
        if (room == null) {
            return ResponseEntity.badRequest().body("Room not found!!");
        }
        return ResponseEntity.ok(room);
    }

    // Create Room
    @Transactional
    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody RoomDto roomDto) {
        try {
            Room newRoom = roomService.addRoom(roomDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(newRoom);
        } catch (RuntimeException e) {
            // Returns 400 Bad Request if user not found or room ID taken
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // Join room
    @Transactional
    @PutMapping("/join")
    public ResponseEntity<?> joinRoom(@RequestBody JoinRoomRequestDto joinRoomDto) {

        try {
            Room updatedRoom = roomService.joinRoom(joinRoomDto.getRoomId(), joinRoomDto.getUserId());
            return ResponseEntity.ok(updatedRoom);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @DeleteMapping("/delete/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable String roomId) {
        roomRepo.deleteById(roomId);
        return ResponseEntity.ok("Room has been deleted");
    }

}

