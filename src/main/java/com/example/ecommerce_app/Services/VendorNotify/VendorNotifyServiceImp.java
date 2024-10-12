package com.example.ecommerce_app.Services.VendorNotify;

import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Notification.DataModel.ProductCreationNotificationRequest;
import com.example.ecommerce_app.Notification.Services.NotificationService;
import com.example.ecommerce_app.Redis.database.RedisDatabaseService;
import com.example.ecommerce_app.Services.User.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Data
@Service
@Slf4j
public class VendorNotifyServiceImp implements VendorNotifyService{

    private final NotificationService notificationService;

    private final RedisDatabaseService redisDatabaseService;

    private final UserService userService;

    @Override
    public void sendProductCreationRequestToAdmin(Product_Creation_Dto product_creation_dto) {
        try {

            ProductCreationNotificationRequest productCreationNotificationRequest =
                    (ProductCreationNotificationRequest) ProductCreationNotificationRequest
                            .builder()
                            .productName(product_creation_dto.getName())
                            .recipientId("admin@gmail.com") // admin id
                            .senderId(Long.toString(product_creation_dto.getVendorId()))
                            .message("Vendor id " + product_creation_dto.getVendorId() + " has requested to add a product")
                            .build();


            redisDatabaseService.addData(
                    "10" , // admin id
                    product_creation_dto
            );

            notificationService.sendNotification(productCreationNotificationRequest);

        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Error while sending product adding request");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
