package com.example.ecommerce_app.Mapper;


import com.example.ecommerce_app.Dto.Brand_Table.BrandCreationDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandResponseDto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Overview_Dto;
import com.example.ecommerce_app.Entity.Brand;
import com.example.ecommerce_app.Entity.Product;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Mapper(
        componentModel = "spring"
)
public interface BrandMapper {

       ProductMapper PRODUCT_MAPPER = Mappers.getMapper(ProductMapper.class);

       @Mapping(source = "image" , target = "image" , qualifiedByName = "mapMultipartFileToBytes")
       Brand fromCreationDtoToEntity(BrandCreationDto brandCreationDto);


       @Mapping(source = "products" , target = "product_overview_dtos" , qualifiedByName = "mapToProduct_overview_dtos")
       BrandResponseDto fromEntityToOverviewDto(Brand brand);


 @Named("mapToProduct_overview_dtos")
 default List<Product_Overview_Dto> mapToProduct_overview_dtos(List<Product> products){
     List<Product_Overview_Dto> product_overview_dtos = new ArrayList<>(products.size());
     for(Product product : products) product_overview_dtos.add(PRODUCT_MAPPER.to_Product_Overview_Dto(product));
    return product_overview_dtos;
 }

 @Named("mapMultipartFileToBytes")
    default byte[] mapMultipartFileToBytes(MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return null; // or handle as needed
        }
        return image.getBytes();
}

}