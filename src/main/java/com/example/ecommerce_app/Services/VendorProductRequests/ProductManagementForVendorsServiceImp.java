package com.example.ecommerce_app.Services.VendorProductRequests;

import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Services.NotificationService.DataModel.NotificationData;
import com.example.ecommerce_app.Services.NotificationService.Services.NotificationService;
import com.example.ecommerce_app.Services.RedisNotificationMessagesManagement.RedisNotificationMessagesManagementService;
import com.example.ecommerce_app.Services.User.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Data
@Service
@Slf4j
public class ProductManagementForVendorsServiceImp implements ProductManagementForVendorsService {

    private final NotificationService notificationService;

    private final UserService userService;

    private final RedisTemplate<String, Object> template;

    private final RedisNotificationMessagesManagementService redisNotificationMessagesManagementService;


    @Override
    public void sendProductCreationRequestToAdmin(Product_Creation_Dto productCreationDto) {
        try {
            String messageId = UUID.randomUUID().toString();
            NotificationData productCreationNotificationRequest = NotificationData
                            .builder()
                            .messageId(messageId)
                            .recipientId("admin@gmail.com")
                            .senderId(Long.toString(productCreationDto.getVendorId()))
                            .message("Vendor id " + productCreationDto.getVendorId() + " has requested to add a product")
                            .build();

            redisNotificationMessagesManagementService.persistNotificationMessageAssociatedData(
                    "admin@gmail.com" ,
                    productCreationDto
            );

            notificationService.sendNotification(productCreationNotificationRequest);

        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Error while sending product adding request");
        }
    }


}