package com.example.ecommerce_app.Mapper;

import com.example.ecommerce_app.Dto.Category_Table.Parent_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.Category_Table.Sub_Category_Creation_Dto;
import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

   @Mappings({
           @Mapping(target = "parentCategory" , ignore = true) ,
           @Mapping( target = "imageFileName", expression = "java(subCategoryCreationDto.getImage().getOriginalFilename())")
   })
   Category toSubCategoryEntity(Sub_Category_Creation_Dto subCategoryCreationDto );


   @Mappings({
           @Mapping( target = "imageFileName" , expression = "java(parentCategoryCreationDto.getImage().getOriginalFilename())")
   })
   Category toParentCategoryEntity(Parent_Category_Creation_Dto parentCategoryCreationDto);





}
