package com.example.ecommerce_app.Dto.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class CartItemQuantityDto {

    private int quantity;

    private long vendorProductId;

    private long customerId;

    private long productId;
}
