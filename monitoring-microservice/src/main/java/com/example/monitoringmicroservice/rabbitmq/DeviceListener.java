package com.example.monitoringmicroservice.rabbitmq;

import com.example.monitoringmicroservice.dtos.DeviceReplicaDTO;
import com.example.monitoringmicroservice.entities.DeviceReplica;
import com.example.monitoringmicroservice.services.DeviceReplicaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class DeviceListener {

    private final DeviceReplicaService service;

    public DeviceListener(DeviceReplicaService service) {
        this.service = service;
    }

    @RabbitListener(queues = "synchronization-broker")
    public void handle(DeviceReplicaDTO dto) {

        switch (dto.getEvent()) {
            case "DEVICE_CREATED":
            case "DEVICE_UPDATED":
                service.createOrUpdate(new DeviceReplica(dto.getId(), dto.getName()));
                break;

            case "DEVICE_DELETED":
                service.delete(dto.getId());
                break;
        }
    }
}
