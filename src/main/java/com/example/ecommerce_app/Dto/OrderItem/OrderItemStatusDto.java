package com.example.ecommerce_app.Dto.OrderItem;

import com.example.ecommerce_app.Entity.Enums.OrderItemStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemStatusDto {

    @NotNull
    private OrderItemStatus orderItemStatus;

    @NotNull
    private long orderItemId;

    @NotNull
    private long vendorProductId;
}
