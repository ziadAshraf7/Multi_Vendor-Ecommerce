package com.example.ecommerce_app.Dto.ProductReview_Table;


import jakarta.persistence.Column;
import jakarta.persistence.Lob;
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
public class ProductReview_Creation_Dto {

    @NotNull
    private long productId;

    @NotNull
    private long userId;

    @DecimalMin(value = "0.5")
    @DecimalMax(value = "5.0")
    private double rate;

    @NotEmpty
    private String description;

    @NotEmpty
    private MultipartFile image;

}
