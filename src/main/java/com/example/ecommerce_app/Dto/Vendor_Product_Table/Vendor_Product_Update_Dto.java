package com.example.ecommerce_app.Dto.Vendor_Product_Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vendor_Product_Update_Dto {

    private int stock;

    private double price;

    private double discount;

}
