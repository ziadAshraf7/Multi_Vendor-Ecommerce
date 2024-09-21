package com.example.ecommerce_app.Dto.Product_Table;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Product_Creation_Dto {

    @NotNull(message = "id can't be null")
    private long vendorId;

    @NotNull(message = "name can't be null")
    private String name;

    @NotNull(message = "rating can't be null")
    @Max(value = 5 , message = "rating cannot be greater than 5 stars")
    @Min(value = 0 , message = "rating cannot be negative")
    private int rating;

    @NotNull(message = "title can't be empty")
    private String title;

    @NotNull(message = "brand can't be empty")
    private long brandId;

    @NotNull(message = "category can't be empty")
    private long subCategoryId;

    @NotNull(message = "thumbNail cannot be null")
    private MultipartFile thumbNail;

    @NotNull(message = "description cannot be null")
    private String description;

    @NotNull(message = "imageFiles cannot be null")
    List<MultipartFile> imageFiles;

    @NotNull(message = "stock cannot be null")
    @Min(value = 0 , message = "stock cannot be negative")
    private int stock;

    @DecimalMin(value = "0.1" , message = "price must be greater than 0.1")
    @NotNull(message = "price cannot be null")
    private double price;

    @DecimalMin(value = "0.0" , message = "discount cannot be negative")
    @NotNull(message = "discount cannot be null")
    private double discount;

}
