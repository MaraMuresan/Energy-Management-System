package com.example.usermanagementmicroservice.dtos;

import java.util.Objects;
import java.util.UUID;

public class UserDTO {
    private UUID id;
    private String username;
    private String address;
    private int age;

    public UserDTO() {
    }

    public UserDTO(UUID id, String username, String address, int age) {
        this.id = id;
        this.username = username;
        this.address = address;
        this.age = age;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return age == userDTO.age &&
                Objects.equals(address,userDTO.address) &&
                Objects.equals(username, userDTO.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, address, age);
    }
}
