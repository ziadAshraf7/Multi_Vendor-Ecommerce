package com.example.ecommerce_app.Services.NotificationService.DataModel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
@Data
@NoArgsConstructor
public class ProductCreationNotificationRequest extends NotificationData {

     private  String productName;

}
