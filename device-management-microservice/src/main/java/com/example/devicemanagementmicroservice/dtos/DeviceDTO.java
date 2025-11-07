package com.example.devicemanagementmicroservice.dtos;

import com.example.devicemanagementmicroservice.entities.UserReplica;

import java.util.Objects;
import java.util.UUID;

public class DeviceDTO {
    private UUID id;
    private String name;
    private double maximumConsumption;

    private UserReplicaDTO userReplicaDTO;

    public DeviceDTO() {
    }

    public DeviceDTO(UUID id, String name, double maximumConsumption, UserReplicaDTO userReplicaDTO) {
        this.id = id;
        this.name = name;
        this.maximumConsumption = maximumConsumption;
        this.userReplicaDTO = userReplicaDTO;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaximumConsumption() {
        return maximumConsumption;
    }

    public void setMaximumConsumption(double maximumConsumption) {
        this.maximumConsumption = maximumConsumption;
    }

    public UserReplicaDTO getUserReplicaDTO() {
        return userReplicaDTO;
    }

    public void setUserReplicaDTO(UserReplicaDTO userReplicaDTO) {
        this.userReplicaDTO = userReplicaDTO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDTO deviceDTO = (DeviceDTO) o;
        return Double.compare(deviceDTO.maximumConsumption, maximumConsumption) == 0 &&
                Objects.equals(name, deviceDTO.name) &&
                Objects.equals(userReplicaDTO, deviceDTO.userReplicaDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, maximumConsumption, userReplicaDTO);
    }
}
