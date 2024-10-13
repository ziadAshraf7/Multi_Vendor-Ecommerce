package com.example.ecommerce_app.Dto.OrderItem;


import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class OrderItemCreationDto {

    @NotNull(message = "product id cannot be null")
    private long productId;

    @NotNull(message = "customer id cannot be null")
    private long customerId;

    @NotNull(message = "order id cannot be null")
    private long orderId;

    @NotNull(message = "quantity cannot be null")
    private int quantity;

    @NotNull(message = "price cannot be null")
    private double price;

}
