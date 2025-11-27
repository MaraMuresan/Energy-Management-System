package com.example.devicemanagementmicroservice.services;

import com.example.devicemanagementmicroservice.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.devicemanagementmicroservice.dtos.DeviceDTO;
import com.example.devicemanagementmicroservice.dtos.DeviceDetailsDTO;
import com.example.devicemanagementmicroservice.dtos.builders.DeviceBuilder;
import com.example.devicemanagementmicroservice.entities.Device;
import com.example.devicemanagementmicroservice.entities.UserReplica;
import com.example.devicemanagementmicroservice.rabbitmq.DevicePublisher;
import com.example.devicemanagementmicroservice.rabbitmq.DeviceReplicaDTO;
import com.example.devicemanagementmicroservice.repositories.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DeviceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);
    private final DeviceRepository deviceRepository;
    private final UserReplicaService userReplicaService;

    private final DevicePublisher devicePublisher;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository, UserReplicaService userReplicaService, DevicePublisher devicePublisher) {
        this.deviceRepository = deviceRepository;
        this.userReplicaService = userReplicaService;
        this.devicePublisher = devicePublisher;
    }

    public List<DeviceDTO> findDevices() {
        List<Device> deviceList = deviceRepository.findAll();

        return deviceList.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public DeviceDTO findDeviceById(UUID id) {
        Optional<Device> deviceOptional = deviceRepository.findById(id);
        if (!deviceOptional.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }
        return DeviceBuilder.toDeviceDTO(deviceOptional.get());
    }

    public List<DeviceDTO> findDevicesByUserId(UUID userId) {
        List<Device> devices = deviceRepository.findByUserId(userId);
        return devices.stream()
                .map(DeviceBuilder::toDeviceDTO)
                .collect(Collectors.toList());
    }

    public UUID insert(DeviceDetailsDTO deviceDTO) {
        Device device = DeviceBuilder.toEntity(deviceDTO);

        if (deviceDTO.getUserId() != null) {
            UserReplica user = userReplicaService
                    .getUserReplica(deviceDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("UserReplica with id " + deviceDTO.getUserId() + " not found"));
            device.setUserReplica(user);
        }

        device = deviceRepository.save(device);
        LOGGER.debug("Device with id {} was inserted in db", device.getId());

        // ASSIGNMENT 2 - PUBLISH TO QUEUE
        DeviceReplicaDTO event = new DeviceReplicaDTO();
        event.setEvent("DEVICE_CREATED");
        event.setId(device.getId());
        event.setName(device.getName());
        devicePublisher.send(event);

        return device.getId();
    }

    public DeviceDTO update(UUID id, DeviceDetailsDTO deviceDTO) {
        Optional<Device> deviceBefore = deviceRepository.findById(id);
        if (!deviceBefore.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        deviceBefore.get().setName(deviceDTO.getName());
        deviceBefore.get().setMaximumConsumption(deviceDTO.getMaximumConsumption());
        deviceBefore.get().setYearOfManufacture(deviceDTO.getYearOfManufacture());

        if (deviceDTO.getUserId() != null) {
            UserReplica user = userReplicaService
                    .getUserReplica(deviceDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("UserReplica with id " + deviceDTO.getUserId() + " not found"));
            deviceBefore.get().setUserReplica(user);
        } else {
            deviceBefore.get().setUserReplica(null);
        }

        Device deviceAfter = deviceRepository.save(deviceBefore.get());
        LOGGER.debug("Device with id {} was updated in db", deviceAfter.getId());

        // ASSIGNMENT 2 - PUBLISH TO QUEUE
        DeviceReplicaDTO event = new DeviceReplicaDTO();
        event.setEvent("DEVICE_UPDATED");
        event.setId(deviceAfter.getId());
        event.setName(deviceAfter.getName());
        devicePublisher.send(event);

        return DeviceBuilder.toDeviceDTO(deviceAfter);
    }

    public void delete(UUID id) {
        Optional<Device> device = deviceRepository.findById(id);
        if (!device.isPresent()) {
            LOGGER.error("Device with id {} was not found in db", id);
            throw new ResourceNotFoundException(Device.class.getSimpleName() + " with id: " + id);
        }

        deviceRepository.delete(device.get());
        LOGGER.debug("Device with id {} was deleted from db", device.get().getId());

        // ASSIGNMENT 2 - PUBLISH TO QUEUE
        DeviceReplicaDTO event = new DeviceReplicaDTO();
        event.setEvent("DEVICE_DELETED");
        event.setId(id);
        devicePublisher.send(event);
    }
}
