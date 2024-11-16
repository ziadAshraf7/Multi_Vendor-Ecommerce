package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Dto.UserEmail.UserEmailDto;
import com.example.ecommerce_app.Services.NotificationService.DataModel.NotificationData;
import com.example.ecommerce_app.Services.RedisNotificationMessagesManagement.RedisNotificationMessagesManagementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@AllArgsConstructor
public class NotificationController {

    private final RedisNotificationMessagesManagementService redisNotificationMessagesManagementService;


    @GetMapping("/getAll")
    public ResponseEntity<List<NotificationData>> getAllMessages(@RequestBody @Valid UserEmailDto userEmailDto){
        return ResponseEntity.ok(redisNotificationMessagesManagementService.getAllNotificationMessages(userEmailDto.getUserEmail()));
    }

    @GetMapping("/getMessageData")
    public ResponseEntity<Object> getMessageData(@RequestParam String notificationMessageData){
        return ResponseEntity.ok(redisNotificationMessagesManagementService.getNotificationMessageAssociatedData(
                notificationMessageData
        ));
    }

}
