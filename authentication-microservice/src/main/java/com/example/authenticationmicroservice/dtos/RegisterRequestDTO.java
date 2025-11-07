package com.example.authenticationmicroservice.dtos;

import com.example.authenticationmicroservice.entities.enums.Role;
import com.example.authenticationmicroservice.dtos.validators.annotation.StrongPassword;

public class RegisterRequestDTO {
    private String username;

    @StrongPassword
    private String password;

    private Role role;

    private String address;

    private int age;

    public RegisterRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }
}
