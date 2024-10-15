package com.example.ecommerce_app.Dto.Order;

import com.example.ecommerce_app.Entity.Enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class OrderUpdateDto {

    private long orderId;

    private Double totalAmount;

    private OrderStatus orderStatus;

}