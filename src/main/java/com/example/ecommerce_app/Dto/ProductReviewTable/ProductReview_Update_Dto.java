package com.example.ecommerce_app.Dto.ProductReviewTable;


import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
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
public class ProductReview_Update_Dto {

    @NotNull(message = "product id cannot be null")
    private long productId;

    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5.0")
    private Double rate;

    private String description;

    private MultipartFile image;

}
