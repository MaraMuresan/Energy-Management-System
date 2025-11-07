package com.example.devicemanagementmicroservice.dtos;

import com.example.devicemanagementmicroservice.dtos.validators.annotation.YearOfManufactureLimit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class DeviceDetailsDTO {

    private UUID id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Double maximumConsumption;

    @YearOfManufactureLimit(limit = 2015)
    private int yearOfManufacture;

    private UUID userId;

    public DeviceDetailsDTO() {
    }

    public DeviceDetailsDTO(String name, Double maximumConsumption, int yearOfManufacture) {
        this.name = name;
        this.maximumConsumption = maximumConsumption;
        this.yearOfManufacture = yearOfManufacture;
    }

    public DeviceDetailsDTO(String name, Double maximumConsumption, int yearOfManufacture, UUID userId) {
        this.name = name;
        this.maximumConsumption = maximumConsumption;
        this.yearOfManufacture = yearOfManufacture;
        this.userId = userId;
    }

    public DeviceDetailsDTO(UUID id, String name, Double maximumConsumption, int yearOfManufacture, UUID userId) {
        this.id = id;
        this.name = name;
        this.maximumConsumption = maximumConsumption;
        this.yearOfManufacture = yearOfManufacture;
        this.userId = userId;
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

    public Double getMaximumConsumption() {
        return maximumConsumption;
    }

    public void setMaximumConsumption(Double maximumConsumption) {
        this.maximumConsumption = maximumConsumption;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public void setYearOfManufacture(int yearOfManufacture) {
        this.yearOfManufacture = yearOfManufacture;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
