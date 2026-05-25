package com.chatapp.lore.service;

import com.chatapp.lore.dto.RoomDto;
import com.chatapp.lore.model.Room;
import com.chatapp.lore.model.User;
import com.chatapp.lore.repo.RoomRepo;
import com.chatapp.lore.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepo roomRepo;
    private final UserRepo userRepo;
    private final UserService userService;

    // Get all rooms
    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    // Get room by id
    public Room getRoom(String roomId) {
        try {
            return roomRepo.findByRoomId(roomId);
        } catch (Exception e) {
            throw new RuntimeException("Room not found");
        }
    }

    public Room addRoom(RoomDto roomDto) {


        // Fetch the existing admin user
        User admin = userRepo.findById(roomDto.getAdminId()).orElseThrow(() -> new RuntimeException("User not found"));

        //3. Setup the room
        Room newRoom = new Room();
        newRoom.setRoomName(roomDto.getRoomName());
        newRoom.getUsers().add(admin);
        roomRepo.save(newRoom);
        return newRoom;

    }

    public Room joinRoom(String roomId, String userId) {
        // 1. Fetch room and user if they exist
        Room room = getRoom(roomId);
        User user = userService.getUser(userId);

        boolean alreadyJoined = room.getUsers().stream().anyMatch(u -> u.getUserId().equals(userId));

        if (alreadyJoined) throw new RuntimeException("User already joined");

        room.getUsers().add(user);
        user.getRooms().add(room);
        return roomRepo.save(room);

    }
}
