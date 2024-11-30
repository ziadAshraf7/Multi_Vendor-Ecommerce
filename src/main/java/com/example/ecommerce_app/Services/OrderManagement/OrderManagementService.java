package com.example.ecommerce_app.Services.OrderManagement;

import com.example.ecommerce_app.Dto.OrderItem.OrderItemBatchSavedDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemStatusDto;

public interface OrderManagementService {
    void makeOrder();
    void changeOrderItemStatus(OrderItemStatusDto orderItemStatusDto);

}
