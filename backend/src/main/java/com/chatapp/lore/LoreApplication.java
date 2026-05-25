package com.chatapp.lore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/")

public class LoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoreApplication.class, args);
        System.out.println("Hello ");
    }

    @GetMapping("/")
    public String check() {
        return "Spring boot server running";
    }
}
