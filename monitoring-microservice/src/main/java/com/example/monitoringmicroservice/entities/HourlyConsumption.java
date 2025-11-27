package com.example.monitoringmicroservice.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "hourly_consumption")
public class HourlyConsumption implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "device_id", nullable = false)
    private UUID deviceId;

    @Column(name = "hour", nullable = false)
    private int hour;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_value", nullable = false)
    private double startValue;

    @Column(name = "consumption", nullable = false)
    private double consumption;

    public HourlyConsumption() {}

    public HourlyConsumption(UUID deviceId, int hour, LocalDate date, double startValue) {
        this.deviceId = deviceId;
        this.hour = hour;
        this.date = date;
        this.startValue = startValue;
        this.consumption = 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(UUID deviceId) {
        this.deviceId = deviceId;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }
}
