package com.example.monitoringmicroservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "device-replica")
public class DeviceReplica implements Serializable {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;
    private Double maximumConsumption;
    private UUID userId;

    public DeviceReplica() {}

    public DeviceReplica(UUID id, String name, Double maximumConsumption, UUID userId) {
        this.id = id;
        this.name = name;
        this.maximumConsumption = maximumConsumption;
        this.userId = userId;
    }

    public UUID getId() { return id; }

    public void setId(UUID id) { this.id = id; }

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