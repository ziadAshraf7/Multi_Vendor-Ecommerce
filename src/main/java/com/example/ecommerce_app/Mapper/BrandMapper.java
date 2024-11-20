package com.example.ecommerce_app.Mapper;


import com.example.ecommerce_app.Dto.Brand_Table.BrandCreationDto;
import com.example.ecommerce_app.Dto.Brand_Table.BrandResponseDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductOverviewDto;
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

       @Mapping( target = "imageFileName" , expression = "java(brandCreationDto.getImage().getOriginalFilename())")
       Brand fromCreationDtoToEntity(BrandCreationDto brandCreationDto);


       @Mapping(source = "products" , target = "product_overview_dtos" , qualifiedByName = "mapToProduct_overview_dtos")
       BrandResponseDto fromEntityToOverviewDto(Brand brand);


 @Named("mapToProduct_overview_dtos")
 default List<ProductOverviewDto> mapToProduct_overview_dtos(List<Product> products){
     List<ProductOverviewDto> product_overview_dtos = new ArrayList<>(products.size());
     for(Product product : products) product_overview_dtos.add(PRODUCT_MAPPER.toProductOverviewDto(product));
    return product_overview_dtos;
 }


}
