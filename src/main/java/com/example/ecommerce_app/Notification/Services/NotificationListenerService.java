package com.example.ecommerce_app.Notification.Services;


import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Notification.DataModel.NotificationData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.Lifecycle;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.example.ecommerce_app.Config.KafkaConfig.NOTIFICATIONS_TOPIC;


@AllArgsConstructor
@Data
@Service
@Slf4j
public class NotificationListenerService {

    private final SimpMessagingTemplate messagingTemplate; // webSocket
    private final KafkaListenerEndpointRegistry registry;

    public void startConsuming() {
        registry.getListenerContainers().forEach(Lifecycle::start);
    }

    public void stopConsuming() {
        registry.getListenerContainers().forEach(Lifecycle::stop);
    }

    @KafkaListener(topics = NOTIFICATIONS_TOPIC , groupId = "notify-0" , autoStartup = "false" )
    private void consumeMessage(ConsumerRecord<String , NotificationData> message  , Acknowledgment acknowledgment) {

        try {
            if(message.value().getRecipientId().equals(message.key())){
                acknowledgment.acknowledge();
                messagingTemplate.convertAndSendToUser(message.value().getRecipientId() ,
                        "queue/notifications"  , message.value());
            }else {
                System.out.println("Not my Message");
            }
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException(e.getMessage());
        }
    }


}
