package com.example.ecommerce_app.Dto.VendorProductTable;

import com.example.ecommerce_app.Entity.VendorProduct;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VendorProductCategoryInfoDto {

    private int stock;

    private double price;

    private double discount;

    private String productName;

    private String productTitle;

    private int rating;

    private String subCategoryName;

}
