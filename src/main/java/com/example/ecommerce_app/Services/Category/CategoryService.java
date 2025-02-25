package com.example.ecommerce_app.Services.Category;

import com.example.ecommerce_app.Dto.Category_Table.CategoryUpdateDto;
import com.example.ecommerce_app.Dto.Category_Table.ParentCategoryCreationDto;
import com.example.ecommerce_app.Dto.Category_Table.SubCategoryCreationDto;
import com.example.ecommerce_app.Entity.Category;

import java.io.IOException;
import java.util.List;

public interface CategoryService {

    List<Category> getSubCategories();

    void addParentCategory(ParentCategoryCreationDto parentCategoryCreationDto) throws IOException;

    void addSubCategory(SubCategoryCreationDto subCategoryCreationDto) throws IOException;

    Category getSubCategoryEntityById(long categoryId);

    Category getParentCategoryEntityById(long categoryId);

    void updateCategory(CategoryUpdateDto categoryUpdateDto) throws IOException;

    Category getCategoryEntityById(long categoryId);

    Category getCategoryEntityByName(String name);

    void deleteCategoryById(long categoryId);
}
