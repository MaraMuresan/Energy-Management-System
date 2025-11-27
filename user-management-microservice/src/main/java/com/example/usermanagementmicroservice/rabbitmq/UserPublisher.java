package com.example.usermanagementmicroservice.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserPublisher {

    private final RabbitTemplate rabbitTemplate;

    public UserPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(UserSynchronizationMessage message) {

        System.out.println(">>> PUBLISHING USER EVENT: "
                + message.getEvent()
                + " id=" + message.getId());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.SYNCHRONIZATION_BROKER,
                message
        );

        System.out.println(">>> MESSAGE SENT TO QUEUE 'synchronization-broker'");
    }
}
