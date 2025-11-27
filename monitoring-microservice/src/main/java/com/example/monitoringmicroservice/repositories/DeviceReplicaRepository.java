package com.example.monitoringmicroservice.repositories;

import com.example.monitoringmicroservice.entities.DeviceReplica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DeviceReplicaRepository extends JpaRepository<DeviceReplica, UUID> {
}