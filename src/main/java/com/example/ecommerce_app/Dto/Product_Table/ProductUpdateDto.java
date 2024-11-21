package com.example.ecommerce_app.Dto.Product_Table;


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
public class ProductUpdateDto {

    @NotNull(message = "id cannot be null")
    private long productId;

    private String name;

    private String title;

    private Integer userRate;

    private MultipartFile thumbNail;
 }
