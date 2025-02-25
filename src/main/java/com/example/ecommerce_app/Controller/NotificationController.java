package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Dto.UserEmail.UserEmailDto;
import com.example.ecommerce_app.Services.NotificationService.DataModel.NotificationData;
import com.example.ecommerce_app.Services.RedisNotificationMessagesManagement.RedisNotificationMessagesManagementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/notification")
@AllArgsConstructor
@Slf4j
public class NotificationController {

    private final RedisNotificationMessagesManagementService redisNotificationMessagesManagementService;

    @GetMapping("/getNotificationMessages")
    public ResponseEntity<List<Object>> getAllNotificationsMessages(@RequestParam  String userEmail  ) throws BadRequestException {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        if(!Objects.equals(((AuthenticatedUserDto) authentication.getPrincipal()).getEmail(), userEmail)) throw new BadRequestException("User is not Authorized");
        return ResponseEntity.ok(redisNotificationMessagesManagementService.getAllNotificationMessages(userEmail));
    }

    @GetMapping("/getMessageData")
    public ResponseEntity<Object> getNotificationMessageAssociatedData(@RequestParam String notificationMessageKey){
        return ResponseEntity.ok(redisNotificationMessagesManagementService.getNotificationMessageAssociatedData(
                notificationMessageKey
        ));
    }

}
