package com.example.ecommerce_app.Dto.VendorProductTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendorProductBrandInfoDto {

    private int stock;

    private double price;

    private double discount;

    private String productName;

    private String productTitle;

    private int rating;

    private String brandId;


}
