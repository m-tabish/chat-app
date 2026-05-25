package com.chatapp.lore.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String roomId;


    private String roomName;

    @ManyToMany
    @JoinTable(
            name = "user_rooms",
            joinColumns = @JoinColumn(name = "room_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
//    @JsonIgnoreProperties("users")
    private Set<User> users = new HashSet<>();


    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMessage> messages = new ArrayList<>();
}
