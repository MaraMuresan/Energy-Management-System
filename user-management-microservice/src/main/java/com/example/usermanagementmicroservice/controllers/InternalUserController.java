package com.example.usermanagementmicroservice.controllers;

import com.example.usermanagementmicroservice.dtos.UserDetailsDTO;
import com.example.usermanagementmicroservice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/internal")
@CrossOrigin
public class InternalUserController {

    private final UserService userService;

    public InternalUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> createUserInternal(@Valid @RequestBody UserDetailsDTO userDTO) {
        userService.insert(userDTO);
        return ResponseEntity.ok().build();
    }
}
