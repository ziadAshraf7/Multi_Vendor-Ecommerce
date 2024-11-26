package com.example.ecommerce_app.Services.RedisNotificationMessagesManagement;

import com.example.ecommerce_app.Services.NotificationService.DataModel.NotificationData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Data
public class RedisNotificationMessagesManagementServiceImp
implements RedisNotificationMessagesManagementService
{
    private  RedisTemplate<String, Object> template = new RedisTemplate<>();

    private static String redisNotificationMessageDataKey = "notificationsMessageAssociatedData:1000";

    @Override
    public void persistNotificationMessage(String userEmail , NotificationData message) {
        template.opsForList().leftPush( userEmail , message);
    }

    @Override
    public void persistNotificationMessageAssociatedData(String notificationMessageId , Object data) {
        template.opsForHash().put(redisNotificationMessageDataKey , notificationMessageId , data );
    }

    @Override
    public void deleteNotificationMessageAssociatedData(String notificationMessageId) {
        template.opsForHash().delete(redisNotificationMessageDataKey
                , notificationMessageId
        );
    }

    @Override
    public Object getNotificationMessageAssociatedData(String notificationMessageId) {
        return template.opsForHash().get(redisNotificationMessageDataKey , notificationMessageId);
    }

    @Override
    public List<Object> getAllNotificationMessages(String userEmail) {
        List<Object> redisData = template.opsForList().range(userEmail, 0, -1 );
        return redisData;
    }
}
