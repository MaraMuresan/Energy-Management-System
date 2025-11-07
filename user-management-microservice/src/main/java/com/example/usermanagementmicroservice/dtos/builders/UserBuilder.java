package com.example.usermanagementmicroservice.dtos.builders;

import com.example.usermanagementmicroservice.dtos.UserDTO;
import com.example.usermanagementmicroservice.dtos.UserDetailsDTO;
import com.example.usermanagementmicroservice.entities.User;
public class UserBuilder {

    private UserBuilder() {
    }

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getAge(), user.getRole());
    }

    public static User toEntity(UserDetailsDTO userDetailsDTO) {
        return new User(userDetailsDTO.getUsername(),
                userDetailsDTO.getAddress(),
                userDetailsDTO.getAge(),
                userDetailsDTO.getPassword(),
                userDetailsDTO.getRole());
    }
}
