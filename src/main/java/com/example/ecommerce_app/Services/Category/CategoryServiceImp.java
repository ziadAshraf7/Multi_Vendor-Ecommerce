package com.example.ecommerce_app.Services.Category;


import com.example.ecommerce_app.Dto.Category_Table.CategoryUpdateDto;
import com.example.ecommerce_app.Dto.Category_Table.Parent_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.Category_Table.Sub_Category_Creation_Dto;
import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Mapper.CategoryMapper;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImp implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;


    public void addParentCategory(Parent_Category_Creation_Dto parentCategoryCreationDto){
        Category category = categoryMapper.toParentCategoryEntity(parentCategoryCreationDto);
        try {
            categoryRepository.save(category);
        }catch (RuntimeException e){

        }
    }

    public void addSubCategory(Sub_Category_Creation_Dto subCategoryCreationDto){

        try {
            Category subCategory = categoryMapper.toSubCategoryEntity(subCategoryCreationDto , categoryRepository);
            categoryRepository.save(subCategory);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
   }

    @Override
    public Category getSubCategoryEntityById(long categoryId) {

        Category subCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("subCategory is not found"));

        Category parentCategory = subCategory.getParentCategory();

        if(parentCategory == null) throw new RuntimeException("category is not subCategory");

        return subCategory;
    }

    @Override
    public Category getParentCategoryEntityById(long categoryId) {
        Category parentCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("subCategory is not found"));

        if(parentCategory.getParentCategory() != null) throw new RuntimeException("category is a subCategory");

        return parentCategory;
    }

    @Override
    public void updateCategory(CategoryUpdateDto categoryUpdateDto) {

        try {
            Category category = categoryRepository.findById(categoryUpdateDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("category is not found"));

            if(categoryUpdateDto.getName() != null) category.setName(category.getName());
            if(categoryUpdateDto.getImage() != null) category.setImage(category.getImage());
            if(categoryUpdateDto.getDescription() != null) category.setDescription(category.getDescription());

            categoryRepository.save(category);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }



    }


}
