package com.example.ecommerce_app.Mapper;

import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.vendorProductImage;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface VendorProductImageMapper {

    @Mapping(source = "imageFiles" , target = "imageFileName" )
    List<vendorProductImage> toEntity(
            List<MultipartFile> imageFiles ,
            @Context Product product ,
            @Context User vendor);



    default vendorProductImage mapMultipartFileToProductImage(MultipartFile file, @Context Product product, @Context User vendor) {
        vendorProductImage productImage = new vendorProductImage();
            productImage.setImageFileName(file.getOriginalFilename());
            productImage.setProduct(product);
            productImage.setVendor(vendor);

         return productImage;
    }

    default List<vendorProductImage> mapImages(List<MultipartFile> files, @Context Product product, @Context User vendor) {
        return files.stream()
                .map(file -> mapMultipartFileToProductImage(file, product, vendor))
                .collect(Collectors.toList());
    }

}
