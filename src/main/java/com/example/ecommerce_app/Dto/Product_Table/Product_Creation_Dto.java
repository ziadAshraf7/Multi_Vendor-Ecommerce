package com.example.ecommerce_app.Dto.Product_Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Product_Creation_Dto implements Serializable {

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

    @JsonIgnore
    @NotNull(message = "thumbNail cannot be null")
    private MultipartFile thumbNail;

    @NotNull(message = "description cannot be null")
    private String description;

    @JsonIgnore
    @NotNull(message = "imageFiles cannot be null")
    @Size(max = 6 , min = 1 , message = "image Files cannot be greater than 6 or less than 1 image")
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

    @NotNull(message = "attributes cannot be empty")
    Map<Long, List<String>> productAttributesWithValues;
}
