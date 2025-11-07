package com.example.devicemanagementmicroservice.services;

import com.example.devicemanagementmicroservice.entities.UserReplica;
import com.example.devicemanagementmicroservice.entities.Device;
import com.example.devicemanagementmicroservice.repositories.DeviceRepository;
import com.example.devicemanagementmicroservice.repositories.UserReplicaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserReplicaService {

    private final UserReplicaRepository userReplicaRepository;
    private final DeviceRepository deviceRepository;

    public UserReplicaService(UserReplicaRepository userReplicaRepository, DeviceRepository deviceRepository) {
        this.userReplicaRepository = userReplicaRepository;
        this.deviceRepository = deviceRepository;
    }

    public void createOrUpdateUserReplica(UserReplica userReplica) {
        Optional<UserReplica> existing = userReplicaRepository.findById(userReplica.getId());
        if (existing.isPresent()) {
            UserReplica user = existing.get();
            user.setUsername(userReplica.getUsername());
            userReplicaRepository.save(user);
        } else {
            userReplicaRepository.save(userReplica);
        }
    }

    public Optional<UserReplica> getUserReplica(UUID id) {
        return userReplicaRepository.findById(id);
    }

    public void deleteUserReplica(UUID id) {
        List<Device> devices = deviceRepository.findAll()
                .stream()
                .filter(d -> d.getUserReplica() != null && id.equals(d.getUserReplica().getId()))
                .collect(Collectors.toList());

        for (Device device : devices) {
            device.setUserReplica(null);
            deviceRepository.save(device);
        }

        if (userReplicaRepository.existsById(id)) {
            userReplicaRepository.deleteById(id);
            System.out.println("Deleted user replica with id " + id);
        }
    }
}