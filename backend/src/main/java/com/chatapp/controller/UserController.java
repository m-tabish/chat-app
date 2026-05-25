package com.chatapp.controller;

import com.chatapp.dto.UserDto;
import com.chatapp.model.User;
import com.chatapp.repo.UserRepo;
import com.chatapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final UserRepo userRepo;

    @GetMapping({"", "/"})
    public ResponseEntity<?> getUsers() {
        List<User> users =  userService.getAllUser();
        return ResponseEntity.ok().body(users);
    }

    // Get user by ids
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUsers(@PathVariable String userId){
        return ResponseEntity.ok(userRepo.getUserByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {

        User newUser = userService.addUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }

}

