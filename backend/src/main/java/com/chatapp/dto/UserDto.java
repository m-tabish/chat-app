package com.chatapp.dto;

import java.time.LocalDateTime;

import com.chatapp.model.UserType;

import lombok.AllArgsConstructor ;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;
    private String contact;
    private String email;
    private String password;
    private UserType userType;
    private LocalDateTime joinedAt = LocalDateTime.now();

    public void setUserType(Object type) {
        if (type instanceof String && ((String) type).equalsIgnoreCase("PUBLIC")) {
            this.userType = UserType.HUMAN;
        } else if (type instanceof UserType) {
            this.userType = (UserType) type;
        } else if (type instanceof String) {
            this.userType = UserType.valueOf(((String) type).toUpperCase());
        }
    }
}

