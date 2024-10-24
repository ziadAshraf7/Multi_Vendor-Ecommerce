package com.example.ecommerce_app.Dto.CategoryAttributeManagement;

import com.example.ecommerce_app.Entity.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class SubCategoryAttributeResponseDto {

    private String subCategoryName;

    private Set<Attribute> attributeNames;

}
