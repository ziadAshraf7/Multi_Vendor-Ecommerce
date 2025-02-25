package com.example.ecommerce_app.Dto.Attribute_Table;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttributeUpdateDto {

    private String newName;

    @NotNull
    private long attributeId;

    private Long newSubCategoryId;

}
