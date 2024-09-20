package com.example.ecommerce_app.Dto.Vendor_Product_Table;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Vendor_Product_Creation_Dto {

    @NotNull
    private long vendorId;

    @NotNull
    private long productId;

    @Min(value = 0)
    @NotNull
    private int stock;

    @DecimalMin(value = "0.1")
    @NotNull
    private double price;

    @DecimalMax(value = "100.0")
    @DecimalMin(value = "0.0")
    @NotNull
    private double discount;

}
