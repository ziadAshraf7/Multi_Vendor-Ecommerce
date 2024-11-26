package com.example.ecommerce_app.Services.RedisNotificationMessagesManagement;

import com.example.ecommerce_app.Services.NotificationService.DataModel.NotificationData;

import java.util.List;

public interface RedisNotificationMessagesManagementService {


    void persistNotificationMessage(String userEmail , NotificationData message);

    void persistNotificationMessageAssociatedData(String notificationMessageId , Object data);

    void deleteNotificationMessageAssociatedData(String notificationMessageId);

    Object getNotificationMessageAssociatedData(String notificationMessageId);

    List<Object> getAllNotificationMessages(String userEmail);
}
