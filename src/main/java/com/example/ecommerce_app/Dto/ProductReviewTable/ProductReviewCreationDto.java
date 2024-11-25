package com.example.ecommerce_app.Dto.ProductReviewTable;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductReviewCreationDto {

    @NotNull(message = "product id cannot be null")
    private long productId;

    @NotNull(message = "user id cannot be null")
    private long userId;

    @NotNull(message = "rate cannot be null")
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5.0")
    private double rate;

    @NotEmpty(message = "description cannot be empty")
    @NotNull(message = "description cannot be null")
    private String description;

    private MultipartFile image;

}
