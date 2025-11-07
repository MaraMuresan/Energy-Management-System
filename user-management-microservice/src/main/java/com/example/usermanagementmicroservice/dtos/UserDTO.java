package com.example.usermanagementmicroservice.dtos;

import com.example.usermanagementmicroservice.entities.enums.Role;

import java.util.Objects;
import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String username;
    private int age;
    private Role role;

    public UserDTO() {
    }

    public UserDTO(UUID id, String username, int age, Role role) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return age == userDTO.age &&
                Objects.equals(username, userDTO.username) &&
                role == userDTO.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, age, role);
    }
}
