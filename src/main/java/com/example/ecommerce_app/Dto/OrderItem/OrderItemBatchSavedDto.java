package com.example.ecommerce_app.Dto.OrderItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItemBatchSavedDto {

    @NotNull(message = "customer id cannot be null")
    private long customerId;

    @NotNull(message = "orderItemGeneralDtoList cannot be null")
    private List<OrderItemGeneralDto> orderItemGeneralDtoList;
}
