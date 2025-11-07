package com.example.devicemanagementmicroservice.controllers;

import com.example.devicemanagementmicroservice.entities.UserReplica;
import com.example.devicemanagementmicroservice.services.UserReplicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/device/internalUser")
public class UserReplicaController {

    private final UserReplicaService userReplicaService;

    public UserReplicaController(UserReplicaService userReplicaService) {
        this.userReplicaService = userReplicaService;
    }

    @PostMapping
    public ResponseEntity<Void> createOrUpdateUser(@RequestBody UserReplica userReplica) {
        userReplicaService.createOrUpdateUserReplica(userReplica);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserReplica(@PathVariable UUID id) {
        userReplicaService.deleteUserReplica(id);
        return ResponseEntity.noContent().build();
    }
}