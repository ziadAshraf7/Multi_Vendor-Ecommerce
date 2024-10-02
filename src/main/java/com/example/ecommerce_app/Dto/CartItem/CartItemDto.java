package com.example.ecommerce_app.Dto.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class CartItemDto {

    private long productId;

    private long vendorId;

    private String name;

    private String title;

    private int rating;

    private byte[] thumbNail;

    private int quantity;

    private double price;

    private double discount;

}
