package com.example.ecommerce_app.Notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.example.ecommerce_app.Config.KafkaConfig.NOTIFICATIONS_TOPIC;


@Service
@AllArgsConstructor
@Data
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate; // webSocket
    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void sendNotification(NotificationData notificationData) throws JsonProcessingException {
        kafkaTemplate.send(NOTIFICATIONS_TOPIC, notificationData.getRecipientId(), notificationData);
    }


}
