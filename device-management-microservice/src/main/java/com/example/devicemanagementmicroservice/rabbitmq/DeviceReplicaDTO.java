package com.example.devicemanagementmicroservice.rabbitmq;


import java.util.UUID;

public class DeviceReplicaDTO {
    private String event;
    private UUID id;
    private String name;
    private Double maximumConsumption;
    private UUID userId;

    public DeviceReplicaDTO() {}

    public DeviceReplicaDTO(UUID id, String name, Double maximumConsumption, UUID userId) {
        this.id = id;
        this.name = name;
        this.maximumConsumption = maximumConsumption;
        this.userId = userId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
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

    public UUID getUserId() { return userId; }

    public void setUserId(UUID userId) { this.userId = userId; }
}
