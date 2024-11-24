package com.example.ecommerce_app.Dto.OrderItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemGeneralDto {

    @NotNull(message = "quantity cannot be null")
    private int quantity;

    @NotNull(message = "vendorProductId cannot be null")
    private long vendorProductId;

    @NotNull(message = "productId cannot be null")
    private long productId;
}
