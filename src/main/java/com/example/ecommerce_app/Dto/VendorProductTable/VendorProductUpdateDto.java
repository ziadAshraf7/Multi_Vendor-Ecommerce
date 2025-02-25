package com.example.ecommerce_app.Dto.VendorProductTable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendorProductUpdateDto {

    @NotNull(message = "VendorProduct id cannot be null ")
    private long vendorProductId;

    private double price;

    private double discount;

}
