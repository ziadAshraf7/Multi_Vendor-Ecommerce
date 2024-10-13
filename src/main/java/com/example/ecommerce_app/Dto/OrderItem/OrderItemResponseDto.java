package com.example.ecommerce_app.Dto.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class OrderItemResponseDto {

    private String productName;

    private byte[] productThumbnail;

    private double price;

    private int quantity;

    private double totalAmount;
}
