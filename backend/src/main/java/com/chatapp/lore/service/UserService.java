package com.chatapp.lore.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.chatapp.lore.dto.UserDto;
import com.chatapp.lore.model.User;
import com.chatapp.lore.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;


    //Get all users
    public List<User> getAllUser() {
        return userRepo.findAll();
    }


    // Get user
    public User getUser(String userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    @Transactional
    public User addUser(UserDto userDto) {
        User user = new User();

        user.setUserType(userDto.getUserType());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setContact(userDto.getContact());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setJoinedAt(LocalDateTime.now());
        userRepo.save(user);
        return user;
    }


    public String deleteUser(String userId) {

        userRepo.deleteById(userId);
        return "User with ID:" + userId + " has been deleted";
//        return "Failed to delete:" + userId;
    }
}
