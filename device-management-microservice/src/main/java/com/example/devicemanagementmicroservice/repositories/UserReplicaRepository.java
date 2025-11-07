package com.example.devicemanagementmicroservice.repositories;

import com.example.devicemanagementmicroservice.entities.UserReplica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserReplicaRepository extends JpaRepository<UserReplica, UUID> {
}