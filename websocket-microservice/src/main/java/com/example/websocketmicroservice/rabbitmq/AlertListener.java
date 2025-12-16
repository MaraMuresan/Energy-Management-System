package com.example.websocketmicroservice.rabbitmq;

import com.example.websocketmicroservice.dtos.AlertNotificationDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertListener {

    private final SimpMessagingTemplate messagingTemplate;

    public AlertListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(
            queues = RabbitMQConfig.NOTIFICATION_BROKER,
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void receive(AlertNotificationDTO alert) {

        System.out.println(">>> RECEIVED NOTIFICATION EVENT: ");
        System.out.println(">>> Event: " + alert.getEvent());
        System.out.println(">>> UserId: " + alert.getUserId());
        System.out.println(">>> DeviceId: " + alert.getDeviceId());
        System.out.println(">>> Hourly consumption: " + alert.getHourlyConsumption());
        System.out.println(">>> Max allowed: " + alert.getMaximumConsumption());

        if (alert.getEvent() == null || !alert.getEvent().startsWith("ALERT_")) {
            return;
        }

        messagingTemplate.convertAndSend(
                "/topic/alerts/" + alert.getUserId(),
                alert
        );
    }
}
