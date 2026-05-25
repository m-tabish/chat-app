package com.chatapp.repo;


import com.chatapp.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends JpaRepository<Room, String> {

     Room findByRoomId(String roomId);
}

