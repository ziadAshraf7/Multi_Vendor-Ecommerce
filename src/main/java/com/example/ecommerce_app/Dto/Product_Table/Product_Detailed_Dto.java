package com.example.ecommerce_app.Dto.Product_Table;

import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Overview_Dto;
import com.example.ecommerce_app.Entity.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product_Detailed_Dto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String title;

    @Min(value = 0)
    private int RatingCount;

    @Max(value = 5)
    @Min(value = 0)
    private int rating;

    private byte[] thumbNail;

    private String brand_name;

    private String subCategoryName;

    private String description;

    private List<Vendor_Product_Overview_Dto> vendor_products_dtos;

    private List<ProductReview_Detailed_Dto> reviewsDtos;

    private  List<byte[]> images;

}
