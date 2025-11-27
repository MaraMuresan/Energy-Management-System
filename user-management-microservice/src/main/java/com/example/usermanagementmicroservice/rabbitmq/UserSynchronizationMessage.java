package com.example.usermanagementmicroservice.rabbitmq;

import java.util.UUID;

public class UserSynchronizationMessage {
    private String event;
    private UUID id;
    private String username;

    public UserSynchronizationMessage() {}

    public UserSynchronizationMessage(String event, UUID id, String username) {
        this.event = event;
        this.id = id;
        this.username = username;
    }

    public String getEvent() { return event; }
    public void setEvent(String event) { this.event = event; }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
