package com.example.ecommerce_app.Dto.CategoryAttributeManagement;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryWithAttributeDto {

    @NotNull
    private long subCategoryId;

    @NotNull
    private long attributeId;

}
