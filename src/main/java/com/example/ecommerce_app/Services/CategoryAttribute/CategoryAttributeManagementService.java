package com.example.ecommerce_app.Services.CategoryAttribute;

import com.example.ecommerce_app.Dto.CategoryAttributeManagement.SubCategoryAttributeResponseDto;
import com.example.ecommerce_app.Dto.CategoryAttributeManagement.SubCategoryWithAttributeDto;

import java.util.List;

public interface CategoryAttributeManagementService {

    void linkBetweenSubCategoryAndAttribute(SubCategoryWithAttributeDto subCategoryWithAttributeDto);

    void unLinkSubCategoryAndAttribute(SubCategoryWithAttributeDto subCategoryWithAttributeDto);

    SubCategoryAttributeResponseDto getSubCategoryAttributesNames(SubCategoryWithAttributeDto subCategoryWithAttributeDto);
}
