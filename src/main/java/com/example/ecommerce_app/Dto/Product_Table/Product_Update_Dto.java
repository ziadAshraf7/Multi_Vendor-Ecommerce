package com.example.ecommerce_app.Dto.Product_Table;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product_Update_Dto {

    @NotNull(message = "id cannot be null")
    private long productId;

    private String name;

    private String title;

    private Integer userRate;

 }
