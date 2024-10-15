package com.example.ecommerce_app.Services.OrderManagement;

import com.example.ecommerce_app.Dto.OrderItem.OrderItemCreationDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemRemovalDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemResponseDto;

import java.util.List;

public interface OrderManagementService {

    void addOrderItem(OrderItemCreationDto orderItemCreationDto);

    void deleteOrderItem(OrderItemRemovalDto orderItemRemovalDto);

    List<OrderItemResponseDto> getOrderItems(long orderId);

    void removeAllOrderItems(long orderId);
}
