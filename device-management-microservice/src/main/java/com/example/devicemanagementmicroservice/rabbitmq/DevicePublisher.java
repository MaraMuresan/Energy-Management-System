package com.example.devicemanagementmicroservice.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class DevicePublisher {

    private final RabbitTemplate rabbitTemplate;

    public DevicePublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(DeviceReplicaDTO dto) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SYNCHRONIZATION_BROKER,
                dto
        );
    }
}
