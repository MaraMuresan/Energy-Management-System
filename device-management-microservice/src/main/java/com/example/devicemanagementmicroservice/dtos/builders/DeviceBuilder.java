package com.example.devicemanagementmicroservice.dtos.builders;

import com.example.devicemanagementmicroservice.dtos.DeviceDTO;
import com.example.devicemanagementmicroservice.dtos.DeviceDetailsDTO;
import com.example.devicemanagementmicroservice.dtos.UserReplicaDTO;
import com.example.devicemanagementmicroservice.entities.Device;
import com.example.devicemanagementmicroservice.entities.UserReplica;

import java.util.UUID;

public class DeviceBuilder {

    private DeviceBuilder() {
    }

    public static DeviceDTO toDeviceDTO(Device device) {
        UserReplicaDTO userReplicaDTO = null;

        if (device.getUserReplica() != null) {
            userReplicaDTO = new UserReplicaDTO(
                    device.getUserReplica().getId(),
                    device.getUserReplica().getUsername() != null ? device.getUserReplica().getUsername() : "Unknown"
            );
        }
        return new DeviceDTO(device.getId(), device.getName(), device.getMaximumConsumption(), device.getYearOfManufacture(), userReplicaDTO);
    }

    public static Device toEntity(DeviceDetailsDTO deviceDetailsDTO) {
        UserReplica userReplica = null;
        if (deviceDetailsDTO.getUserId() != null) {
            userReplica = new UserReplica();
            userReplica.setId(deviceDetailsDTO.getUserId());
        }
        return new Device(deviceDetailsDTO.getName(),
                deviceDetailsDTO.getMaximumConsumption(),
                deviceDetailsDTO.getYearOfManufacture(),
                userReplica);
    }
}
