package com.example.monitoringmicroservice.services;

import com.example.monitoringmicroservice.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.monitoringmicroservice.dtos.DeviceMeasurementDTO;
import com.example.monitoringmicroservice.dtos.HourlyConsumptionDTO;
import com.example.monitoringmicroservice.dtos.builders.HourlyConsumptionBuilder;
import com.example.monitoringmicroservice.entities.HourlyConsumption;
import com.example.monitoringmicroservice.repositories.HourlyConsumptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonitoringService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringService.class);

    private final HourlyConsumptionRepository repository;
    private final DeviceReplicaService deviceReplicaService;

    @Autowired
    public MonitoringService(HourlyConsumptionRepository repository, DeviceReplicaService deviceReplicaService) {
        this.repository = repository;
        this.deviceReplicaService = deviceReplicaService;
    }

    public void processMeasurement(DeviceMeasurementDTO dto) {
        if (!deviceReplicaService.exists(dto.getDeviceId())) {
            LOGGER.warn("Received measurement for unknown device {} -> ignoring", dto.getDeviceId());
            return;
        }
        UUID deviceId = dto.getDeviceId();
        LocalDateTime ts = LocalDateTime.parse(dto.getTimestamp());
        LocalDate date = ts.toLocalDate();
        int hour = ts.getHour();
        double currentValue = dto.getMeasurementValue();

        Optional<HourlyConsumption> existingHour =
                repository.findSpecificHour(deviceId, date, hour);

        if (existingHour.isEmpty()) {
            HourlyConsumption entry = new HourlyConsumption(
                    deviceId,
                    hour,
                    date,
                    currentValue
            );
            repository.save(entry);
            LOGGER.info("Starting hour {} for device {} with startValue {}", hour, deviceId, currentValue);
            return;
        }

        HourlyConsumption entry = existingHour.get();

        double startValue = entry.getStartValue();
        double hourlyTotal = currentValue - startValue;

        if (hourlyTotal < 0) hourlyTotal = 0;

        entry.setConsumption(hourlyTotal);
        repository.save(entry);

        LOGGER.info("Hour {} device {}: start={} current={} total={}",
                hour, deviceId, startValue, currentValue, hourlyTotal);
    }

    public List<HourlyConsumptionDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(HourlyConsumptionBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public List<HourlyConsumptionDTO> findByDeviceId(UUID deviceId) {
        return repository.findByDeviceId(deviceId)
                .stream()
                .map(HourlyConsumptionBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public List<HourlyConsumptionDTO> findByDeviceAndDate(UUID deviceId, LocalDate date) {
        return repository.findByDeviceIdAndDate(deviceId, date)
                .stream()
                .map(HourlyConsumptionBuilder::toDTO)
                .collect(Collectors.toList());
    }

    public HourlyConsumptionDTO findSpecificHour(UUID deviceId, LocalDate date, int hour) {
        Optional<HourlyConsumption> hourOpt = repository.findSpecificHour(deviceId, date, hour);

        if (!hourOpt.isPresent()) {
            LOGGER.error("No hourly entry found for device {} date {} hour {}", deviceId, date, hour);
            throw new ResourceNotFoundException("No hourly consumption entry found.");
        }

        return HourlyConsumptionBuilder.toDTO(hourOpt.get());
    }
}
