package com.chatapp.lore.repo;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.chatapp.lore.model.ChatMessage;

@Repository
public interface MessageRepo extends JpaRepository<ChatMessage, String> {

    List<ChatMessage> findByRoom_RoomIdOrderBySentAtAsc(String roomId);

}
