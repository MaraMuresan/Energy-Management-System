package com.example.monitoringmicroservice.services;

import com.example.monitoringmicroservice.dtos.DeviceMeasurementDTO;
import com.example.monitoringmicroservice.rabbitmq.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitConsumerService {

    private final MonitoringService monitoringService;


    public RabbitConsumerService(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @RabbitListener(
            queues = RabbitMQConfig.DATA_COLLECTION_BROKER,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void receiveMessage(DeviceMeasurementDTO dto) {
        monitoringService.processMeasurement(dto);
    }
}
