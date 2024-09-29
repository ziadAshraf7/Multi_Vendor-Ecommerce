package com.example.ecommerce_app.Dto.CartItem.Session;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionCartItemsDto {

    private long productID;

    private long vendorId;

    private String name;

    private String title;

    private byte[] thumbNail;

    private int stock;

    private double price;

    private double discount;

    private int quantity;
}
