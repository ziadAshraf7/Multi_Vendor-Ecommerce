package com.example.ecommerce_app.Dto.VendorProductTable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendorProductDeleteDto {

    private long vendorId;

    private long productId;

}
