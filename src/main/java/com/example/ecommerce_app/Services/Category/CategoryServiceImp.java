package com.example.ecommerce_app.Services.Category;


import com.example.ecommerce_app.Dto.Category_Table.CategoryUpdateDto;
import com.example.ecommerce_app.Dto.Category_Table.Parent_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.Category_Table.Sub_Category_Creation_Dto;
import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Mapper.CategoryMapper;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import com.example.ecommerce_app.Services.FileSystemStorage.FileSystemStorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImp implements CategoryService {


    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final FileSystemStorageService fileSystemStorageService;

    @Transactional
    public void addParentCategory(Parent_Category_Creation_Dto parentCategoryCreationDto) throws IOException {
        Category existingCategoryByName = getCategoryEntityByName(parentCategoryCreationDto.getName());
        if(existingCategoryByName != null) throw new CustomConflictException("Category with name " + parentCategoryCreationDto.getName() + " is already exists" );

        Category category = categoryMapper.toParentCategoryEntity(parentCategoryCreationDto);
        category.setImageFileName(
                fileSystemStorageService.saveImageToFileSystem(parentCategoryCreationDto.getImage())
        );
        try {
            categoryRepository.save(category);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to add category with name " + parentCategoryCreationDto.getName()  );
        }
    }

    @Transactional
    public void addSubCategory(Sub_Category_Creation_Dto subCategoryCreationDto) throws IOException {
            Category existingCategoryByName = getCategoryEntityByName(subCategoryCreationDto.getName());
            if(existingCategoryByName != null) throw new CustomConflictException("Category with name " + subCategoryCreationDto.getName() + " is already exists" );

            Category parentCategory = getParentCategoryEntityById(subCategoryCreationDto.getParentCategoryId());
            Category subCategory = categoryMapper.toSubCategoryEntity(subCategoryCreationDto);
            subCategory.setParentCategory(parentCategory);
            subCategory.setImageFileName(
                    fileSystemStorageService.saveImageToFileSystem(subCategoryCreationDto.getImage())
            );
        try {
            categoryRepository.save(subCategory);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to add sub_category with name " + subCategoryCreationDto.getName());
        }
   }

    @Override
    @Transactional(readOnly = true)
    public Category getSubCategoryEntityById(long categoryId) {

        Category subCategory = getCategoryEntityById(categoryId);

        Category parentCategory = subCategory.getParentCategory();

        if(parentCategory == null) throw new CustomConflictException(" category with id " + categoryId + " is not a sub_category");

        return subCategory;
    }

    @Override
    @Transactional(readOnly = true)
    public Category getParentCategoryEntityById(long categoryId) {
        Category parentCategory = getCategoryEntityById(categoryId);

        if(parentCategory.getParentCategory() != null) throw new CustomConflictException("category with id "  + categoryId + " is a sub_category");

        return parentCategory;
    }

    @Override
    @Transactional
    public void updateCategory(CategoryUpdateDto categoryUpdateDto) throws IOException {

            Category category = getCategoryEntityById(categoryUpdateDto.getCategoryId());

            if(categoryUpdateDto.getName() != null) category.setName(category.getName());
            if(categoryUpdateDto.getImage() != null) {
                fileSystemStorageService.deleteImageFile(category.getImageFileName());
                category.setImageFileName(fileSystemStorageService.saveImageToFileSystem(
                        categoryUpdateDto.getImage()
                ));
            }
            if(categoryUpdateDto.getDescription() != null) category.setDescription(category.getDescription());

        try {
            categoryRepository.save(category);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to update category with id " + categoryUpdateDto.getCategoryId() );
        }

    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryEntityById(long categoryId) {
          return categoryRepository.findById(categoryId).orElseThrow(
                    () -> new CustomNotFoundException("Unable to find category with category id " + categoryId)
            );
         }

    @Transactional(readOnly = true)
    @Override
    public Category getCategoryEntityByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Transactional
    @Override
    public void deleteCategoryById(long categoryId) {
        Category category = getCategoryEntityById(categoryId);
        fileSystemStorageService.deleteImageFile(category.getImageFileName());
        try {
            categoryRepository.deleteById(category.getId());
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Database error while deleting category");
        }

    }



}
