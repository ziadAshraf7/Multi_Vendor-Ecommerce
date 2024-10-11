package com.example.ecommerce_app.EventListners;

import com.example.ecommerce_app.Notification.NotificationListenerService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@AllArgsConstructor
public class WebSocketEventListener implements ApplicationListener<SessionConnectEvent> {


    private final NotificationListenerService notificationListenerService;

    @Override
    public void onApplicationEvent(SessionConnectEvent event) {
        notificationListenerService.startConsuming();
        System.out.println("user connected");
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        notificationListenerService.stopConsuming();
        System.out.println("User disconnected: " + event.getSessionId());

    }
}