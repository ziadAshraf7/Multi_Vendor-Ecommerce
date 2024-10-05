package com.example.ecommerce_app.ListenerAdabters;

import com.example.ecommerce_app.Config.RabbitMqConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqReceiver {


//    @RabbitListener(queues = RabbitMqConfig.queueName)
//    public void receiveMessage(String message) {
//        System.out.println("Received message: " + message);
//    }

}
