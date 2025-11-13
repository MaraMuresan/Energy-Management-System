package com.example.usermanagementmicroservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.usermanagementmicroservice.dtos.UserDTO;
import com.example.usermanagementmicroservice.dtos.UserDetailsDTO;
import com.example.usermanagementmicroservice.services.UserService;

import  jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/user")
@Tag(name = "User", description = "CRUD operations for user management")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    @Operation(
            summary = "Get all users",
            description = "Returns a list of all users stored in the system."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of users returned successfully")
    })
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<UserDTO> dtos = userService.findUsers();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping()
    @Operation(
            summary = "Create a new user",
            description = "Creates a new user and returns the UUID of the created user."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    public ResponseEntity<UUID> insertUser(
            @Valid @RequestBody
            @Parameter(description = "User details for creation") UserDetailsDTO userDTO) {
        UUID userID = userService.insert(userDTO);
        return new ResponseEntity<>(userID, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Returns user information for the specified user ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUser(
            @PathVariable("id")
            @Parameter(description = "UUID of the user to be retrieved") UUID userId) {
        UserDTO dto = userService.findUserById(userId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    @Operation(
            summary = "Update user",
            description = "Updates data for an existing user identified by its UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable("id")
            @Parameter(description = "UUID of the user to be updated") UUID userId,
            @Valid @RequestBody
            @Parameter(description = "Updated user details") UserDetailsDTO userDTO) {
        UserDTO dto = userService.update(userId, userDTO);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete user",
            description = "Removes a user from the system by its UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id")
            @Parameter(description = "UUID of the user to be deleted") UUID userId) {
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
