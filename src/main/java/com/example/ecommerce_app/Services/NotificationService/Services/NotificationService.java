package com.example.ecommerce_app.Services.NotificationService.Services;

import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Services.NotificationService.DataModel.NotificationData;
import com.example.ecommerce_app.Services.RedisNotificationMessagesManagement.RedisNotificationMessagesManagementService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.ecommerce_app.Config.KafkaConfig.NOTIFICATIONS_TOPIC;


@Service
@AllArgsConstructor
@Data
@Slf4j
public class NotificationService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final RedisNotificationMessagesManagementService redisNotificationMessagesManagementService;

    public void sendNotification(NotificationData notificationData) {
        try {
            kafkaTemplate.send(NOTIFICATIONS_TOPIC, notificationData.getRecipientId(), notificationData);
            redisNotificationMessagesManagementService.persistNotificationMessage(
                    notificationData.getRecipientId(),
                    notificationData
            );
        }catch (CustomConflictException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Error while sending notification message with associated data");
        }

    }

}
