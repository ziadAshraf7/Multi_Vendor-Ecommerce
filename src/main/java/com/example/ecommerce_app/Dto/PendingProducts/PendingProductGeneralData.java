package com.example.ecommerce_app.Dto.PendingProducts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class PendingProductGeneralData {

    private  String vendorId;

    private  String productName;
}
