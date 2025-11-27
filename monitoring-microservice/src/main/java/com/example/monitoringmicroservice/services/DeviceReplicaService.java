package com.example.monitoringmicroservice.services;

import com.example.monitoringmicroservice.entities.DeviceReplica;
import com.example.monitoringmicroservice.repositories.DeviceReplicaRepository;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class DeviceReplicaService {

    private final DeviceReplicaRepository deviceReplicaRepository;

    public DeviceReplicaService(DeviceReplicaRepository deviceReplicaRepository) {
        this.deviceReplicaRepository = deviceReplicaRepository;
    }

    public void createOrUpdate(DeviceReplica replica) {
        deviceReplicaRepository.save(replica);
    }

    public void delete(UUID id) {
        deviceReplicaRepository.deleteById(id);
    }

    public boolean exists(UUID id) {
        return deviceReplicaRepository.existsById(id);
    }
}