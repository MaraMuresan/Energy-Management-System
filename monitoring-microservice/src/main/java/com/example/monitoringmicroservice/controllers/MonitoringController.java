package com.example.monitoringmicroservice.controllers;

import com.example.monitoringmicroservice.dtos.HourlyConsumptionDTO;
import com.example.monitoringmicroservice.services.MonitoringService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(value = "/monitoring")
public class MonitoringController {

    private final MonitoringService monitoringService;

    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @GetMapping("/all")
    public List<HourlyConsumptionDTO> getAll() {
        return monitoringService.findAll();
    }

    @GetMapping("/device/{id}")
    public List<HourlyConsumptionDTO> getByDevice(@PathVariable UUID id) {
        return monitoringService.findByDeviceId(id);
    }

    @GetMapping("/device/{id}/day/{date}")
    public List<HourlyConsumptionDTO> getByDeviceAndDay(
            @PathVariable UUID id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        return monitoringService.findByDeviceAndDate(id, date);
    }

    @GetMapping("/device/{id}/day/{date}/hour/{hour}")
    public HourlyConsumptionDTO getSpecificHour(
            @PathVariable UUID id,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @PathVariable int hour) {

        return monitoringService.findSpecificHour(id, date, hour);
    }
}
