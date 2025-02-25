package com.example.ecommerce_app.Mapper;

import java.util.Base64;

import com.example.ecommerce_app.Dto.Attribute_Table.AttributeDto;
import com.example.ecommerce_app.Dto.ProductAttributeValueTable.ProductAttributeValueDto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.Product_Table.ProductCreationDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductDetailedDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductOverviewDto;
import com.example.ecommerce_app.Dto.VendorProductTable.VendorProductOverviewDto;
import com.example.ecommerce_app.Entity.*;
import java.util.List;

import com.example.ecommerce_app.Entity.Embedded_Ids.VendorProductEmbeddedId;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Mapper(
        componentModel = "spring" ,
        imports = {Vendor_Product_Mapper.class ,
                VendorProduct.class , VendorProductEmbeddedId.class , List.class
        , Base64.class}
)
public interface ProductMapper {

    Vendor_Product_Mapper VENDOR_PRODUCT_MAPPER = Mappers.getMapper(Vendor_Product_Mapper.class);

    ProductReview_Mapper PRODUCT_REVIEW_MAPPER = Mappers.getMapper(ProductReview_Mapper.class);

    @Mapping(target = "RatingCount" , ignore = true)
    @Mapping(target = "thumbNail"  , expression = "java(product_creation_dto.getThumbNail().getOriginalFilename())")
    @Mapping(target = "brand" , ignore = true )
    @Mapping(target = "subCategory" , ignore = true)
    @Mapping(target = "imageFilesName" , source = "imageFiles" , qualifiedByName = "mapFromImagesFilesToImages")
    Product toEntity(ProductCreationDto product_creation_dto);


    @Mappings({
            @Mapping( target = "brandName" , expression = "java(product.getBrandName())") ,
            @Mapping(target = "subCategoryName" , expression = "java(product.getSubCategoryName())") ,
            @Mapping(source = "vendor_products" , target = "vendorProductsDto" , qualifiedByName = "mapToVendorProductDto") ,
            @Mapping(target = "productId" , expression = "java(product.getId())")
    })
    ProductOverviewDto toProductOverviewDto(Product product);


    @Mappings({
            @Mapping( target = "brandName" , expression = "java(product.getBrandName())") ,
            @Mapping( target = "subCategoryName" , expression = "java(product.getSubCategoryName())") ,
            @Mapping(source = "vendor_products" , target = "vendorProductOverviewDtos" , qualifiedByName = "mapToVendorProductDto") ,
            @Mapping(source = "imageFilesName" , target = "imageFilesName"  , qualifiedByName = "mapToImages") ,
            @Mapping(source = "reviews" , target = "reviewsDtos" , qualifiedByName = "mapTo_ProductReview_Detailed_Dto"),
            @Mapping(source = "attributeValues" , target = "attributeDtoListMap" , qualifiedByName = "mapToAttributeValuesDetails"),
    })
    ProductDetailedDto toProductDetailedDto(Product product);

    @Named("mapToAttributeValuesDetails")
    default Map<String, List<ProductAttributeValueDto>>  mapToAttributeValuesDetails(
            List<ProductAttributeValue> attributeValues){

        Map<String, List<ProductAttributeValueDto>> map = new HashMap<>();

        attributeValues.forEach(productAttributeValue -> {
            AttributeDto attributeDto = AttributeDto.builder()
                    .attributeId(productAttributeValue.getAttribute().getId())
                    .name(productAttributeValue.getAttribute().getName())
                    .build();

            ProductAttributeValueDto productAttributeValueDto = ProductAttributeValueDto
                    .builder()
                    .value(productAttributeValue.getValue())
                    .build();

            map.compute(attributeDto.getName() , (key , value) -> {
                if(value == null) return new ArrayList<>(List.of(productAttributeValueDto));
                value.add(productAttributeValueDto);
                return value;
            });
        });

        return map;
    }

    @Named("mapTo_ProductReview_Detailed_Dto")
    default List<ProductReview_Detailed_Dto>  mapTo_ProductReview_Detailed_Dto(List<ProductReview> reviews){
        List<ProductReview_Detailed_Dto> productReview_detailed_dtos = new ArrayList<>(reviews.size());
        for(ProductReview productReview : reviews) productReview_detailed_dtos.add(PRODUCT_REVIEW_MAPPER.to_ProductReview_Detailed_Dto(productReview));
        return productReview_detailed_dtos;
    }


    @Named("mapToImage")
    default String mapToImage(MultipartFile image) { return image.getOriginalFilename();};


    @Named("mapToImages")
    default List<String> mapToImages(List<vendorProductImage> images){
        List<String> imageFiles = new ArrayList<>(images.size());

        for(vendorProductImage vendor_product_image : images){
            imageFiles.add(vendor_product_image.getImageFileName());
        }
        return imageFiles;
    }

    @Named("mapFromImagesFilesToImages")
    default List<vendorProductImage> mapFromImagesFilesToImages(List<MultipartFile> imageFiles) throws IOException {

        List<vendorProductImage> images = new ArrayList<>(imageFiles.size());

        for(MultipartFile image : imageFiles){
            images.add(vendorProductImage.builder().imageFileName(image.getOriginalFilename()).build());
        }
        return images;
    }

    @Named("mapToVendorProductDto")
    default List<VendorProductOverviewDto> mapToVendorProductDto(List<VendorProduct> vendor_products){
        List<VendorProductOverviewDto> vendor_product_overview_dtos = new ArrayList<>(vendor_products.size());

        for(VendorProduct vendor_product : vendor_products){
            vendor_product_overview_dtos.add(VENDOR_PRODUCT_MAPPER.toVendorProductOverviewDto(vendor_product));
        }
        return vendor_product_overview_dtos;
    }

}
