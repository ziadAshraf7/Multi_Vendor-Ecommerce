package com.example.ecommerce_app.Dto.CartItem.Session;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SessionUserCartItemQuantityDto {
    private int quantity;

    private long vendorId;

    private long productId;

    private double productPrice;
}
