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

    @NotNull
    private long vendorId;

    @NotEmpty
    private String name;

    @NotNull
    @Max(value = 5)
    @Min(value = 0)
    private int rating;

    @NotEmpty
    private String title;

    @NotNull(message = "brand can't be empty")
    private long brandId;

    @NotNull(message = "category can't be empty")
    private long subCategoryId;

    @NotNull
    private MultipartFile thumbNail;

    @NotNull
    private String description;

    @NotNull
    List<MultipartFile> imageFiles;

    @NotNull
    @Min(0)
    private int stock;

    @DecimalMin(value = "0.1")
    private double price;

    @DecimalMin(value = "0.0")
    private double discount;

}
