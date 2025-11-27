package com.example.monitoringmicroservice.dtos;

import java.util.UUID;

public class DeviceReplicaDTO {
    private String event;
    private UUID id;
    private String name;

    public DeviceReplicaDTO() {}

    public DeviceReplicaDTO(UUID id, String name) {
        this.id = id;
        this.name = name;
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
}
