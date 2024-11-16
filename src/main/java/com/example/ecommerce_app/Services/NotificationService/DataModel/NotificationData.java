package com.example.ecommerce_app.Services.NotificationService.DataModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
@SuperBuilder
public class NotificationData implements Serializable {

    private String messageId;

    private String recipientId;

    private String message;

    private String senderId;
}
