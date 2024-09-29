package com.example.ecommerce_app.Dto.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemSessionDto {

    private long productId;

    private long vendorId;

    private int quantity;
}
