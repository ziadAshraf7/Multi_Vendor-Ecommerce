package com.example.ecommerce_app.Services.SubCategoryAttribute;

import com.example.ecommerce_app.Dto.CategoryAttributeManagement.SubCategoryAttributeResponseDto;
import com.example.ecommerce_app.Dto.CategoryAttributeManagement.SubCategoryWithAttributeDto;
import com.example.ecommerce_app.Entity.Attribute;
import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabaseInternalServerError;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Attribute.AttributeRepository;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import com.example.ecommerce_app.Services.Attribute.AttributeService;
import com.example.ecommerce_app.Services.Category.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class CategoryAttributeManagementServiceImp implements CategoryAttributeManagementService{

    private final CategoryService categoryService;

    private final CategoryRepository categoryRepository;

    private final AttributeService attributeService;

    private final AttributeRepository attributeRepository;

    @Transactional
    @Override
    public void linkBetweenSubCategoryAndAttribute(SubCategoryWithAttributeDto subCategoryWithAttributeDto) {

            Category subCategory = categoryService.getSubCategoryEntityById(subCategoryWithAttributeDto.getSubCategoryId());

            Attribute attribute = attributeService.getAttributeEntityById(subCategoryWithAttributeDto.getAttributeId());

            if(attribute.getSubCategories().contains(subCategory)) throw new CustomBadRequestException("attribute is already linked with category");

            attribute.addNewSubCategory(subCategory);

        try {
            attributeRepository.save(attribute);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabaseInternalServerError("Database error while linking between attribute and sub_Category");
        }

    }

    @Transactional
    @Override
    public void unLinkSubCategoryAndAttribute(SubCategoryWithAttributeDto subCategoryWithAttributeDto) {

            Category subCategory = categoryService.getSubCategoryEntityById(subCategoryWithAttributeDto.getSubCategoryId());

            Attribute attribute = attributeService.getAttributeEntityById(subCategoryWithAttributeDto.getAttributeId());

            subCategory.removeAttribute(attribute);

        try {
            categoryRepository.save(subCategory);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabaseInternalServerError("Database error while linking between attribute and sub_Category");
        }

    }

    @Transactional(readOnly = true)
    @Override
    public SubCategoryAttributeResponseDto getSubCategoryAttributesNames(long categoryId ) {

            Category subCategory = categoryService.getSubCategoryEntityById(categoryId);

            Set<Attribute> attributes = subCategory.getSubCategoryAttributes();

            return SubCategoryAttributeResponseDto.builder()
                    .attributeNames(attributes)
                    .subCategoryName(subCategory.getName())
                    .build();
    }
}
