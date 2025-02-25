package com.example.ecommerce_app.Mapper;

import com.example.ecommerce_app.Dto.Category_Table.ParentCategoryCreationDto;
import com.example.ecommerce_app.Dto.Category_Table.SubCategoryCreationDto;
import com.example.ecommerce_app.Entity.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

   @Mappings({
           @Mapping(target = "parentCategory" , ignore = true) ,
           @Mapping( target = "imageFileName", expression = "java(subCategoryCreationDto.getImage().getOriginalFilename())")
   })
   Category toSubCategoryEntity(SubCategoryCreationDto subCategoryCreationDto );


   @Mappings({
           @Mapping( target = "imageFileName" , expression = "java(parentCategoryCreationDto.getImage().getOriginalFilename())")
   })
   Category toParentCategoryEntity(ParentCategoryCreationDto parentCategoryCreationDto);





}
