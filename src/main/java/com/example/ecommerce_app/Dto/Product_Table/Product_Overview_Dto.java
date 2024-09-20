package com.example.ecommerce_app.Dto.Product_Table;

import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Overview_Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product_Overview_Dto {

    private String name;

    private String title;

    private int rating;

    private String brand_name;

    private String sub_category_name;

    private byte[] thumbNail;

    private List<Vendor_Product_Overview_Dto> vendor_products_dtos;
}
