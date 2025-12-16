package com.example.monitoringmicroservice.rabbitmq;

import com.example.monitoringmicroservice.dtos.AlertNotificationDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertPublisher {

    private final RabbitTemplate rabbitTemplate;

    public AlertPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(AlertNotificationDTO alert) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTIFICATION_BROKER,
                alert
        );

    }
}
