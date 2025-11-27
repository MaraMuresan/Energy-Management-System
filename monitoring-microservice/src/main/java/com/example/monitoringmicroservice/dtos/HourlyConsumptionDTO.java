package com.example.monitoringmicroservice.dtos;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class HourlyConsumptionDTO {

    private UUID id;
    private UUID deviceId;
    private int hour;
    private LocalDate date;
    private double consumption;

    public HourlyConsumptionDTO() {}

    public HourlyConsumptionDTO(UUID id, UUID deviceId, int hour, LocalDate date, double consumption) {
        this.id = id;
        this.deviceId = deviceId;
        this.hour = hour;
        this.date = date;
        this.consumption = consumption;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public int getHour() {
        return hour;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HourlyConsumptionDTO)) return false;
        HourlyConsumptionDTO that = (HourlyConsumptionDTO) o;
        return hour == that.hour &&
                Double.compare(that.consumption, consumption) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(deviceId, that.deviceId) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, deviceId, hour, date, consumption);
    }
}
