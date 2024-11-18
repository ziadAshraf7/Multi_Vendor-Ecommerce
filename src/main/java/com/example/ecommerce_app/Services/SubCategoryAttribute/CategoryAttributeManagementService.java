package com.example.ecommerce_app.Services.SubCategoryAttribute;

import com.example.ecommerce_app.Dto.CategoryAttributeManagement.SubCategoryAttributeResponseDto;
import com.example.ecommerce_app.Dto.CategoryAttributeManagement.SubCategoryWithAttributeDto;

public interface CategoryAttributeManagementService {

    void linkBetweenSubCategoryAndAttribute(SubCategoryWithAttributeDto subCategoryWithAttributeDto);

    void unLinkSubCategoryAndAttribute(SubCategoryWithAttributeDto subCategoryWithAttributeDto);

    SubCategoryAttributeResponseDto getSubCategoryAttributesNames(SubCategoryWithAttributeDto subCategoryWithAttributeDto);
}
