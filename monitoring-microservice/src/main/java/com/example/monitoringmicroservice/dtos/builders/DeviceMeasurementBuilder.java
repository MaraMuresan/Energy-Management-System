package com.example.monitoringmicroservice.dtos.builders;

import com.example.monitoringmicroservice.dtos.DeviceMeasurementDTO;

import java.util.UUID;

public class DeviceMeasurementBuilder {

    private DeviceMeasurementBuilder() {}

    public static DeviceMeasurementDTO fromJsonValues(String timestamp, UUID deviceId, double value) {
        return new DeviceMeasurementDTO(timestamp, deviceId, value);
    }
}
