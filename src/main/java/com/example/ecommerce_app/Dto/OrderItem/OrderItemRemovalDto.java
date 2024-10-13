package com.example.ecommerce_app.Dto.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class OrderItemRemovalDto {

    private long orderId;

    private long productId;

    private long customerId;
}
