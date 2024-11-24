package com.example.ecommerce_app.Dto.Order;

import com.example.ecommerce_app.Entity.Enums.OrderItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class OrderResponseDto {

    private String customerName;

    private OrderItemStatus status ;

    private double totalAmount ;

}
