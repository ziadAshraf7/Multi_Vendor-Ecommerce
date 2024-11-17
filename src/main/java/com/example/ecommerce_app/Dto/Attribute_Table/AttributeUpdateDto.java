package com.example.ecommerce_app.Dto.Attribute_Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttributeUpdateDto {

    private String newName;

    private long attributeId;

    private Long newSubCategoryId;

}
