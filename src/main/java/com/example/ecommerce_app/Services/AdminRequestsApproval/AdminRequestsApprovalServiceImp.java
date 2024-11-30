package com.example.ecommerce_app.Services.AdminRequestsApproval;


import com.example.ecommerce_app.Dto.ProductRejectionRequest.ProductRejectionRequestDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductCreationDto;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Projections.User.UserGeneralInfoInfoView;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Services.NotificationService.DataModel.PendingProductApprovalNotificationMessage;
import com.example.ecommerce_app.Services.NotificationService.DataModel.ProductApprovalStatus;
import com.example.ecommerce_app.Services.NotificationService.Services.NotificationService;
import com.example.ecommerce_app.Services.ProductManagement.ProductManagementService;
import com.example.ecommerce_app.Services.RedisNotificationMessagesManagement.RedisNotificationMessagesManagementService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@AllArgsConstructor
@Data
@Service
@Slf4j
public class AdminRequestsApprovalServiceImp implements AdminRequestsApprovalService {

    private final NotificationService notificationService;


    private final ProductManagementService productManagementService;

    private final RedisTemplate<String, Object> template;

    private final RedisNotificationMessagesManagementService redisNotificationMessagesManagementService;

    private final UserRepository userRepository;

    @Override
    public void rejectProduct(ProductRejectionRequestDto productRejectionRequestDto)  {

        UserGeneralInfoInfoView vendor = userRepository.findGeneralInfoById(productRejectionRequestDto.getVendorId());

       try {

           redisNotificationMessagesManagementService.deleteNotificationMessageAssociatedData(
                   productRejectionRequestDto.getNotificationMessageId()
           );

           PendingProductApprovalNotificationMessage pendingProductApprovalNotificationMessage = PendingProductApprovalNotificationMessage
                   .builder()
                   .productApprovalStatus(ProductApprovalStatus.REJECTED)
                   .recipientId(vendor.getEmail())
                   .message("your request for " + productRejectionRequestDto.getProductName()  + " creating request has been rejected from the Admin")
                   .build();

           notificationService.sendNotification(pendingProductApprovalNotificationMessage);

       }catch (CustomRuntimeException e){
           log.error(e.getMessage());
           throw new CustomRuntimeException("error while trying to reject the product creation request");
       }

    }

    @Transactional
    @Override
    public void acceptProduct(ProductCreationDto productCreationDto , String notificationMessageId) throws IOException {
        UserGeneralInfoInfoView vendor = userRepository.findGeneralInfoById(productCreationDto.getVendorId());

        try {
            redisNotificationMessagesManagementService.deleteNotificationMessageAssociatedData(
                    notificationMessageId
            );
        }catch (CustomRuntimeException e){
            throw new CustomRuntimeException("Unable to find any associated data with this messageId");
        }


        productManagementService.addProduct(productCreationDto);

        PendingProductApprovalNotificationMessage pendingProductApprovalNotificationMessage =  PendingProductApprovalNotificationMessage
                .builder()
                .productApprovalStatus(ProductApprovalStatus.ACCEPTED)
                .recipientId(vendor.getEmail())
                .message("your request for " + productCreationDto.getName()  + " creating request has been Successfully accepted from the Admin")
                .build();

        notificationService.sendNotification(pendingProductApprovalNotificationMessage);

    }

}
