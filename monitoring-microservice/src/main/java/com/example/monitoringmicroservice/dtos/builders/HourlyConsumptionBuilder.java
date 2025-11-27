package com.example.monitoringmicroservice.dtos.builders;

import com.example.monitoringmicroservice.dtos.HourlyConsumptionDTO;
import com.example.monitoringmicroservice.entities.HourlyConsumption;

public class HourlyConsumptionBuilder {

    private HourlyConsumptionBuilder() {}

    public static HourlyConsumptionDTO toDTO(HourlyConsumption entity) {
        return new HourlyConsumptionDTO(
                entity.getId(),
                entity.getDeviceId(),
                entity.getHour(),
                entity.getDate(),
                entity.getConsumption()
        );
    }

    public static HourlyConsumption toEntity(HourlyConsumptionDTO dto) {
        HourlyConsumption entity = new HourlyConsumption();
        entity.setId(dto.getId());
        entity.setDeviceId(dto.getDeviceId());
        entity.setHour(dto.getHour());
        entity.setDate(dto.getDate());
        entity.setConsumption(dto.getConsumption());
        return entity;
    }
}
