package com.example.ecommerce_app.Dto.Vendor_Product_Table;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vendor_Product_Update_Dto {

    @NotNull(message = "product id cannot be null")
    private long productId;

    @NotNull(message = "vendor id cannot be null")
    private long vendorId;

    private int stock;

    private double price;

    private double discount;

}
