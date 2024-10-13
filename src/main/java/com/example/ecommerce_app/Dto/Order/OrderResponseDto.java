package com.example.ecommerce_app.Dto.Order;

import com.example.ecommerce_app.Entity.Enums.OrderStatus;
import com.example.ecommerce_app.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class OrderResponseDto {

    private String customerName;

    private OrderStatus status ;

    private double totalAmount ;

}
