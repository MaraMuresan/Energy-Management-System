package com.example.usermanagementmicroservice.controllers;

import com.example.usermanagementmicroservice.dtos.UserDetailsDTO;
import com.example.usermanagementmicroservice.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/internal")
@CrossOrigin
@Tag(name = "Internal Users", description = "Endpoint used internally by Authentication Microservice")
public class InternalUserController {

    private final UserService userService;

    public InternalUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(
            summary = "Create user (internal)",
            description = "Used internally by the Authentication microservice to sync user data."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    public ResponseEntity<Void> createUserInternal(
            @Valid @RequestBody
            @Parameter(description = "User details to be stored internally") UserDetailsDTO userDTO) {
        userService.insert(userDTO);
        return ResponseEntity.ok().build();
    }
}
