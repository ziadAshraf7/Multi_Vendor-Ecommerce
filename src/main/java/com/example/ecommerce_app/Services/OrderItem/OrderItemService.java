package com.example.ecommerce_app.Services.OrderItem;

import com.example.ecommerce_app.Dto.OrderItem.OrderItemResponseDto;
import com.example.ecommerce_app.Dto.OrderItem.OrderItemStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderItemService {

    List<OrderItemResponseDto> getOrderItems(long orderId);

    void removeAllOrderItems(long orderId);

    Page<OrderItemResponseDto> getOrderItemsByVendorId(long vendorId , Pageable pageable);


}
