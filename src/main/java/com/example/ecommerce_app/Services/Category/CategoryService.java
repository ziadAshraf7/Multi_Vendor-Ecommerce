package com.example.ecommerce_app.Services.Category;

import com.example.ecommerce_app.Dto.Category_Table.CategoryUpdateDto;
import com.example.ecommerce_app.Dto.Category_Table.Parent_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.Category_Table.Sub_Category_Creation_Dto;
import com.example.ecommerce_app.Entity.Category;

public interface CategoryService {

    void addParentCategory(Parent_Category_Creation_Dto parentCategoryCreationDto);

    void addSubCategory(Sub_Category_Creation_Dto subCategoryCreationDto);

    Category getSubCategoryEntityById(long categoryId);

    Category getParentCategoryEntityById(long categoryId);

    void updateCategory(CategoryUpdateDto categoryUpdateDto);

    Category getCategoryEntityById(long categoryId);

}
