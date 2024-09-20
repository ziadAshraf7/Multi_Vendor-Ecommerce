package com.example.ecommerce_app.Mapper;


import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Detailed_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Overview_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Overview_Dto;
import com.example.ecommerce_app.Entity.*;
import java.util.List;

import com.example.ecommerce_app.Entity.Embedded_Ids.Vendor_Product_EmbeddedId;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Mapper(
        componentModel = "spring" ,
        imports = {Vendor_Product_Mapper.class , Vendor_Product.class , Vendor_Product_EmbeddedId.class , List.class}
)
public interface ProductMapper {

    Vendor_Product_Mapper VENDOR_PRODUCT_MAPPER = Mappers.getMapper(Vendor_Product_Mapper.class);

    ProductReview_Mapper PRODUCT_REVIEW_MAPPER = Mappers.getMapper(ProductReview_Mapper.class);

    @Mapping(target = "RatingCount" , ignore = true)
    @Mapping(target = "thumbNail" , source = "thumbNail" , qualifiedByName = "mapToImage")
    @Mapping(target = "brand" , ignore = true )
    @Mapping(target = "subCategory" , ignore = true)
    @Mapping(target = "vendor_products",  expression = "java(List.of(Vendor_Product.builder().id(new Vendor_Product_EmbeddedId()).build()))")
    @Mapping(target = "images" , source = "imageFiles" , qualifiedByName = "mapFromImagesFilesToImages")
    Product toEntity(Product_Creation_Dto product_creation_dto);


    @Mappings({
            @Mapping( target = "brand_name" , expression = "java(product.getBrandName())") ,
            @Mapping(target = "sub_category_name" , expression = "java(product.getSubCategoryName())") ,
            @Mapping(source = "vendor_products" , target = "vendor_products_dtos" , qualifiedByName = "mapToVendorProductDto") ,
    })
    Product_Overview_Dto to_Product_Overview_Dto(Product product);


    @Mappings({
            @Mapping( target = "brand_name" , expression = "java(product.getBrandName())") ,
            @Mapping( target = "subCategoryName" , expression = "java(product.getSubCategoryName())") ,
            @Mapping(source = "vendor_products" , target = "vendor_products_dtos" , qualifiedByName = "mapToVendorProductDto") ,
            @Mapping(source = "images" , target = "images" , qualifiedByName = "mapToImages") ,
            @Mapping(source = "reviews" , target = "reviewsDtos" , qualifiedByName = "mapTo_ProductReview_Detailed_Dto")
    })
    Product_Detailed_Dto to_Product_Detailed_Dto(Product product);


    @Named("mapTo_ProductReview_Detailed_Dto")
    default List<ProductReview_Detailed_Dto>  mapTo_ProductReview_Detailed_Dto(List<ProductReview> reviews){
        List<ProductReview_Detailed_Dto> productReview_detailed_dtos = new ArrayList<>(reviews.size());
        for(ProductReview productReview : reviews) productReview_detailed_dtos.add(PRODUCT_REVIEW_MAPPER.to_ProductReview_Detailed_Dto(productReview));
        return productReview_detailed_dtos;
    }



    @Named("mapToImage")
    default byte[] mapToImage(MultipartFile image) throws IOException { return image.getBytes();};


    @Named("mapToImages")
    default List<byte[]> mapToImages(List<Vendor_Product_Image> images){
        List<byte[]> imageFiles = new ArrayList<>(images.size());

        for(Vendor_Product_Image vendor_product_image : images){
            imageFiles.add(vendor_product_image.getImage());
        }
        return imageFiles;
    }

    @Named("mapFromImagesFilesToImages")
    default List<Vendor_Product_Image> mapFromImagesFilesToImages(List<MultipartFile> imageFiles) throws IOException {

        List<Vendor_Product_Image> images = new ArrayList<>(imageFiles.size());

        for(MultipartFile image : imageFiles){
            images.add(Vendor_Product_Image.builder().image(image.getBytes()).build());
        }
        return images;

    }

    @Named("mapToVendorProductDto")
    default List<Vendor_Product_Overview_Dto> mapToVendorProductDto(List<Vendor_Product> vendor_products){
        List<Vendor_Product_Overview_Dto> vendor_product_overview_dtos = new ArrayList<>(vendor_products.size());

        for(Vendor_Product vendor_product : vendor_products){
            vendor_product_overview_dtos.add(VENDOR_PRODUCT_MAPPER.to_Vendor_Product_Overview_Dto(vendor_product));
        }

        return vendor_product_overview_dtos;
    }

}
