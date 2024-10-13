package com.example.ecommerce_app.Services.Order;

import com.example.ecommerce_app.Dto.Order.OrderResponseDto;
import com.example.ecommerce_app.Dto.Order.OrderUpdateDto;
import com.example.ecommerce_app.Entity.Enums.OrderStatus;

public interface OrderService {

    void createOrder(long customerId);

    OrderResponseDto getOrderData(long orderId);

    void removeOrder(long orderId);

    void updateOrder(OrderUpdateDto orderUpdateDto);

    }
