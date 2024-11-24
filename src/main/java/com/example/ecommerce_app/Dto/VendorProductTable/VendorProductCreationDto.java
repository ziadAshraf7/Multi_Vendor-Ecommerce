package com.example.ecommerce_app.Dto.VendorProductTable;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class VendorProductCreationDto {

    @NotNull(message = "user id cannot be null")
    private long vendorId;

    @NotNull(message = "product id cannot be null")
    private long productId;

    @Min(value = 0)
    @NotNull(message = "stock cannot be null")
    private int stock;

    @DecimalMin(value = "0.1")
    @NotNull(message = "price cannot be null")
    private double price;

    @DecimalMax(value = "100.0")
    @DecimalMin(value = "0.0")
    @NotNull(message = "discount cannot be null")
    private double discount;

}
