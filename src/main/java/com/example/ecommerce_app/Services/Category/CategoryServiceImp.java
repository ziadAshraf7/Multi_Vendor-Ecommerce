package com.example.ecommerce_app.Services.Category;


import com.example.ecommerce_app.Dto.Category_Table.CategoryUpdateDto;
import com.example.ecommerce_app.Dto.Category_Table.Parent_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.Category_Table.Sub_Category_Creation_Dto;
import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotFoundException;
import com.example.ecommerce_app.Mapper.CategoryMapper;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImp implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;


    @Transactional
    public void addParentCategory(Parent_Category_Creation_Dto parentCategoryCreationDto){
        Category category = categoryMapper.toParentCategoryEntity(parentCategoryCreationDto);
        try {
            categoryRepository.save(category);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to add category with name " + parentCategoryCreationDto.getName()  );
        }
    }

    @Transactional
    public void addSubCategory(Sub_Category_Creation_Dto subCategoryCreationDto){
        try {
            Category parentCategory = getParentCategoryEntityById(subCategoryCreationDto.getParentCategoryId());
            Category subCategory = categoryMapper.toSubCategoryEntity(subCategoryCreationDto);
            subCategory.setParentCategory(parentCategory);
            categoryRepository.save(subCategory);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to add sub_category with name " + subCategoryCreationDto.getName());
        }
   }

    @Override
    @Transactional(readOnly = true)
    public Category getSubCategoryEntityById(long categoryId) {

        Category subCategory = getCategoryEntityById(categoryId);

        Category parentCategory = subCategory.getParentCategory();

        if(parentCategory == null) throw new CustomRuntimeException(" category with id " + categoryId + " is not a sub_category");

        return subCategory;
    }

    @Override
    @Transactional(readOnly = true)
    public Category getParentCategoryEntityById(long categoryId) {
        Category parentCategory = getCategoryEntityById(categoryId);

        if(parentCategory.getParentCategory() != null) throw new CustomRuntimeException("category with id "  + categoryId + " is a sub_category");

        return parentCategory;
    }

    @Override
    @Transactional
    public void updateCategory(CategoryUpdateDto categoryUpdateDto) {

        try {
            Category category = getCategoryEntityById(categoryUpdateDto.getCategoryId());

            if(categoryUpdateDto.getName() != null) category.setName(category.getName());
            if(categoryUpdateDto.getImage() != null) category.setImage(category.getImage());
            if(categoryUpdateDto.getDescription() != null) category.setDescription(category.getDescription());

            categoryRepository.save(category);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to update category with id " + categoryUpdateDto.getCategoryId() );
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryEntityById(long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Unable to find Category with id " + categoryId));
    }


}
