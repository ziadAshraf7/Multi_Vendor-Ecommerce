package com.example.ecommerce_app.Dto.ProductRejectionRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductRejectionRequestDto {


    private long vendorId;

    private String productName;

    private String notificationMessageId;

}
