package com.chatapp.repo;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.chatapp.model.ChatMessage;

@Repository
public interface MessageRepo extends JpaRepository<ChatMessage, String> {

    List<ChatMessage> findByRoom_RoomIdOrderBySentAtAsc(String roomId);

}

