package com.example.ecommerce_app.Config;

import com.example.ecommerce_app.ListenerAdabters.RabbitMqReceiver;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
@Data
@EnableRabbit  // Enable RabbitMQ support
public class RabbitMqConfig {
    // Define the queue, exchange, and routing key constants
    public static final String NOTIFICATION_QUEUE = "notifications.queue";
    public static final String NOTIFICATION_EXCHANGE = "notifications.exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notifications.routing.key";

    // Define the queue bean with TTL and optional dead-letter exchange
    @Bean
    public Queue notificationQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-message-ttl", 60000); // TTL of 1 minute
        args.put("x-dead-letter-exchange", "dead-letter-exchange"); // Optional dead-letter exchange

        return new Queue(NOTIFICATION_QUEUE, true, false, false, args);
    }

    // Define the notification exchange bean
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }

    // Define the dead-letter exchange bean
    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange("dead-letter-exchange");
    }

    // Binding the queue to the exchange
    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(notificationExchange)
                .with(NOTIFICATION_ROUTING_KEY);
    }

    // Optional: Message listener configuration
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                                    MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(NOTIFICATION_QUEUE); // Use the correct queue name here
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(RabbitMqReceiver receiver) {
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }
}
