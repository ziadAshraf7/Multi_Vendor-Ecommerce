package com.example.ecommerce_app.Dto.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CartItemCreationDto {

    private long productId;

    private long vendorId;

    private long customerId;

    private int quantity;

}
