package com.example.monitoringmicroservice.dtos;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class AlertNotificationDTO {

    private String event;
    private UUID userId;
    private UUID deviceId;
    private Double hourlyConsumption;
    private Double maximumConsumption;
    private String message;
    private LocalDateTime timestamp;

    public AlertNotificationDTO() {
    }

    public AlertNotificationDTO(UUID userId, UUID deviceId, Double hourlyConsumption,
                                Double maximumConsumption, String message, LocalDateTime timestamp) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.hourlyConsumption = hourlyConsumption;
        this.maximumConsumption = maximumConsumption;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public Double getHourlyConsumption() {
        return hourlyConsumption;
    }

    public void setHourlyConsumption(Double hourlyConsumption) {
        this.hourlyConsumption = hourlyConsumption;
    }

    public Double getMaximumConsumption() {
        return maximumConsumption;
    }

    public void setMaximumConsumption(Double maximumConsumption) {
        this.maximumConsumption = maximumConsumption;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlertNotificationDTO)) return false;
        AlertNotificationDTO that = (AlertNotificationDTO) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, deviceId, message);
    }
}
