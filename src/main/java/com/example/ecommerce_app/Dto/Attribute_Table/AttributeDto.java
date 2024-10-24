package com.example.ecommerce_app.Dto.Attribute_Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AttributeDto {

    private String name;

    private long attributeId;
}
