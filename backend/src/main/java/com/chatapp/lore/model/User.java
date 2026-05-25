package com.chatapp.lore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private UserType userType;


    private String contact;
    private String email;

    private String password;

    @Column(name = "joined_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime joinedAt = LocalDateTime.now();


    @ManyToMany(mappedBy = "users")
    @JsonIgnore
    private List<Room> rooms = new ArrayList<Room>();
}
