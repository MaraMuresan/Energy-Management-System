package com.example.usermanagementmicroservice.dtos;

import com.example.usermanagementmicroservice.dtos.validators.annotation.AgeLimit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;


public class UserDetailsDTO {

    private UUID id;

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String address;

    @AgeLimit(limit = 18)
    private int age;

    public UserDetailsDTO() {
    }

    public UserDetailsDTO(String username, String address, int age) {
        this.username = username;
        this.address = address;
        this.age = age;
    }

    public UserDetailsDTO(UUID id, String username, String address, int age) {
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
}
