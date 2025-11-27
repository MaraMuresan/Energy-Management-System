package com.example.devicemanagementmicroservice.rabbitmq;

import com.example.devicemanagementmicroservice.entities.UserReplica;
import com.example.devicemanagementmicroservice.services.UserReplicaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserListener {

    private final UserReplicaService service;
    private final ObjectMapper mapper;

    public UserListener(UserReplicaService service, ObjectMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RabbitListener(
            queues = RabbitMQConfig.SYNCHRONIZATION_BROKER,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void handle(UserSynchronizationMessage user) {

        System.out.println(">>> RECEIVED USER EVENT: " + user.getEvent() + " id=" + user.getId());

        if (user.getEvent() == null || !user.getEvent().startsWith("USER_")) {
            return;
        }

        switch (user.getEvent()) {
            case "USER_CREATED":
            case "USER_UPDATED":
                service.createOrUpdateUserReplica(
                        new UserReplica(user.getId(), user.getUsername())
                );
                break;

            case "USER_DELETED":
                service.deleteUserReplica(user.getId());
                break;
        }
    }
}
