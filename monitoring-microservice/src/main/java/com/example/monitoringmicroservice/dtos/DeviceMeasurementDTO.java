package com.example.monitoringmicroservice.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

public class DeviceMeasurementDTO {

    @JsonProperty("device_id")
    private UUID deviceId;

    @JsonProperty("timestamp")
    private String timestamp;

    @JsonProperty("measurement_value")
    private double measurementValue;

    public DeviceMeasurementDTO() {}

    public DeviceMeasurementDTO(String timestamp, UUID deviceId, double measurementValue) {
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.measurementValue = measurementValue;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public double getMeasurementValue() {
        return measurementValue;
    }

    public void setMeasurementValue(double measurementValue) {
        this.measurementValue = measurementValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceMeasurementDTO that = (DeviceMeasurementDTO) o;
        return Double.compare(that.measurementValue, measurementValue) == 0 &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(deviceId, that.deviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, deviceId, measurementValue);
    }
}
