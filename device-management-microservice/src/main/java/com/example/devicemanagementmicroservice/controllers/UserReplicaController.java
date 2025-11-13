package com.example.devicemanagementmicroservice.controllers;

import com.example.devicemanagementmicroservice.entities.UserReplica;
import com.example.devicemanagementmicroservice.services.UserReplicaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/device/internalUser")
@Tag(name = "Internal User Replica", description = "Internal endpoints for user replication")
public class UserReplicaController {

    private final UserReplicaService userReplicaService;

    public UserReplicaController(UserReplicaService userReplicaService) {
        this.userReplicaService = userReplicaService;
    }

    @PostMapping
    @Operation(
            summary = "Create or update user replica",
            description = "Internal endpoint used by User Microservice to synchronize user data."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User replica created or updated")
    })
    public ResponseEntity<Void> createOrUpdateUser(
            @RequestBody
            @Parameter(description = "User replica data to store or update") UserReplica userReplica) {
        userReplicaService.createOrUpdateUserReplica(userReplica);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user replica",
            description = "Removes a user replica entity by its UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User replica deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User replica not found")
    })
    public ResponseEntity<Void> deleteUserReplica(
            @PathVariable
            @Parameter(description = "UUID of the replicated user to delete") UUID id) {
        userReplicaService.deleteUserReplica(id);
        return ResponseEntity.noContent().build();
    }
}