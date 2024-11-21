package com.example.ecommerce_app.Services.Category;

import com.example.ecommerce_app.Dto.Category_Table.CategoryUpdateDto;
import com.example.ecommerce_app.Dto.Category_Table.Parent_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.Category_Table.Sub_Category_Creation_Dto;
import com.example.ecommerce_app.Entity.Category;

import java.io.IOException;

public interface CategoryService {

    void addParentCategory(Parent_Category_Creation_Dto parentCategoryCreationDto) throws IOException;

    void addSubCategory(Sub_Category_Creation_Dto subCategoryCreationDto) throws IOException;

    Category getSubCategoryEntityById(long categoryId);

    Category getParentCategoryEntityById(long categoryId);

    void updateCategory(CategoryUpdateDto categoryUpdateDto) throws IOException;

    Category getCategoryEntityById(long categoryId);

    Category getCategoryEntityByName(String name);

    void deleteCategoryById(long categoryId);
}
