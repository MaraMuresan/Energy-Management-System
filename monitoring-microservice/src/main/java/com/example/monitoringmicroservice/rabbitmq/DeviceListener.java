package com.example.monitoringmicroservice.rabbitmq;

import com.example.monitoringmicroservice.dtos.DeviceReplicaDTO;
import com.example.monitoringmicroservice.entities.DeviceReplica;
import com.example.monitoringmicroservice.services.DeviceReplicaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DeviceListener {

    private final DeviceReplicaService service;
    private final ObjectMapper mapper;

    public DeviceListener(DeviceReplicaService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(
            queues = RabbitMQConfig.SYNCHRONIZATION_BROKER,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handle(DeviceReplicaDTO dto) {

        System.out.println(">>> RECEIVED DEVICE EVENT: " + dto.getEvent() + " id=" + dto.getId());

        if (dto.getEvent() == null || !dto.getEvent().startsWith("DEVICE_")) {
            return;
        }

        switch (dto.getEvent()) {
            case "DEVICE_CREATED":
            case "DEVICE_UPDATED":
                service.createOrUpdate(new DeviceReplica(dto.getId(), dto.getName(), dto.getMaximumConsumption(), dto.getUserId()));
                break;

            case "DEVICE_DELETED":
                service.delete(dto.getId());
                break;
        }
    }
}
