package com.example.ecommerce_app.Dto.Attribute_Table;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
public class AttributeCreationDto {

    @NotNull
    private String attributeName;

}
