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
           @Mapping(source = "image", target = "image", qualifiedByName = "mapMultipartFileToBytes")
   })
   Category toSubCategoryEntity(Sub_Category_Creation_Dto subCategoryCreationDto );


   @Mappings({
           @Mapping(source = "image", target = "image", qualifiedByName = "mapMultipartFileToBytes")
   })
   Category toParentCategoryEntity(Parent_Category_Creation_Dto parentCategoryCreationDto);



   @Named("mapMultipartFileToBytes")
   default byte[] mapMultipartFileToBytes(MultipartFile image) throws IOException {
      if (image == null || image.isEmpty()) {
         return null; // or handle as needed
      }
      return image.getBytes();
   }

}
