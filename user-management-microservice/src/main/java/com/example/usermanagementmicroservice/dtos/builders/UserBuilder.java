package com.example.usermanagementmicroservice.dtos.builders;

import com.example.usermanagementmicroservice.dtos.UserDTO;
import com.example.usermanagementmicroservice.dtos.UserDetailsDTO;
import com.example.usermanagementmicroservice.entities.User;
public class UserBuilder {

    private UserBuilder() {
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getAddress(), user.getAge());
    }

    public static User toEntity(UserDetailsDTO userDetailsDTO) {
        User user = new User();
        user.setId(userDetailsDTO.getId());
        user.setUsername(userDetailsDTO.getUsername());
        user.setAddress(userDetailsDTO.getAddress());
        user.setAge(userDetailsDTO.getAge());
        return user;
    }
}
