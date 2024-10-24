package com.example.ecommerce_app.Dto.ProductAttributeValueTable;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor

public class ProductAttributeValueRemovalDto {

    @NotNull
    private long productId;

    @NotNull
    private long attributeId;

    @NotNull
    private long valueId;
}
