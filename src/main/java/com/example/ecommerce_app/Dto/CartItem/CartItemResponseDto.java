package com.example.ecommerce_app.Dto.CartItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemResponseDto {

    private String productName;

    private String title;

    private String thumbNail;

    private double price;

    private long productId;

    private int quantity;

    private long vendorProductId;

}
