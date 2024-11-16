package com.example.ecommerce_app.Dto.CartItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@AllArgsConstructor
@Data
@NoArgsConstructor
@SuperBuilder
public class CartItemDto implements Serializable {

    @NotNull(message = "productId cannot be null")
    private long productId;

    @NotNull(message = "vendorProductId cannot be null")
    private long vendorProductId;

    @NotNull(message = "quantity cannot be null")
    private int quantity;

}
