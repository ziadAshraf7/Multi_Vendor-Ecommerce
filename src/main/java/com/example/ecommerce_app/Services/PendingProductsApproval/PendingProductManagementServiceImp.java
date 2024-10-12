package com.example.ecommerce_app.Services.PendingProductsApproval;


import com.example.ecommerce_app.Dto.PendingProducts.PendingProductGeneralData;
import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Notification.DataModel.PendingProductApprovalNotification;
import com.example.ecommerce_app.Notification.DataModel.ProductApprovalStatus;
import com.example.ecommerce_app.Notification.Services.NotificationService;
import com.example.ecommerce_app.Redis.database.RedisDatabaseService;
import com.example.ecommerce_app.Services.Product_Mangement.Product_Management_Service;
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
public class PendingProductManagementServiceImp implements PendingProductApprovalService {

    private final NotificationService notificationService;

    private final UserService userService;

    private final Product_Management_Service productManagementService;

    private final RedisDatabaseService redisDatabaseService;


    @Override
    public void rejectProduct(String vendorId, String productName) throws JsonProcessingException {

       User vendor = userService.getUserEntityById(Long.parseLong(vendorId));

       try {
           redisDatabaseService.clearData("10" ,
                   PendingProductGeneralData
                           .builder()
                           .vendorId(vendorId)
                           .productName(productName)
                           .build());

           notificationService.sendNotification(PendingProductApprovalNotification
                   .builder()
                   .productApprovalStatus(ProductApprovalStatus.REJECTED)
                   .recipientId(vendor.getEmail())
                   .message("your request for " + productName  + " creating request has been rejected from the Admin")
                   .build());

       }catch (RuntimeException e){
           log.error(e.getMessage());
           throw new RuntimeException("error while trying to reject the product creation request");
       }

    }

    @Override
    public void acceptProduct(Product_Creation_Dto product_creation_dto) throws JsonProcessingException {
        try {
            User vendor = userService.getUserEntityById(product_creation_dto.getVendorId());

            redisDatabaseService.clearData(
                    "10" , // admin id
                    PendingProductGeneralData.builder()
                            .vendorId(Long.toString(product_creation_dto.getVendorId()))
                            .productName(product_creation_dto.getName())
                            .build()
            );

            productManagementService.addProduct(product_creation_dto); // persist it to database

            // sending notification
            notificationService.sendNotification(PendingProductApprovalNotification
                    .builder()
                    .productApprovalStatus(ProductApprovalStatus.ACCEPTED)
                    .recipientId(vendor.getEmail())
                    .message("your request for " + product_creation_dto.getName()  + " creating request has been Successfully accepted from the Admin")
                    .build());
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new RuntimeException("error while trying to accept the product creation request");
        }


    }

}
