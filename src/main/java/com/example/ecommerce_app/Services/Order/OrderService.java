package com.example.ecommerce_app.Services.Order;

import com.example.ecommerce_app.Dto.Order.OrderResponseDto;
import com.example.ecommerce_app.Dto.Order.OrderUpdateDto;

public interface OrderService {
    OrderResponseDto getOrderData(long orderId);
}
