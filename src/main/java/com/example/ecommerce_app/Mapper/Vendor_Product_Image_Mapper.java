package com.example.ecommerce_app.Mapper;

import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.Vendor_Product_Image;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface Vendor_Product_Image_Mapper {

    @Mapping(source = "imageFiles" , target = "image" )
    List<Vendor_Product_Image> toEntity(
            List<MultipartFile> imageFiles ,
            @Context Product product ,
            @Context User vendor);


    default byte[] mapMultipartFileToByteArray(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            // Handle the exception as needed
            return null;
        }
    }

    default Vendor_Product_Image mapMultipartFileToProductImage(MultipartFile file, @Context Product product, @Context User vendor) {
        Vendor_Product_Image productImage = new Vendor_Product_Image();
        try {
            productImage.setImage(file.getBytes());
            productImage.setProduct(product); // Set the product from context
            productImage.setVendor(vendor);   // Set the vendor from context
        } catch (Exception e) {
            // Handle exception
        }
        return productImage;
    }

    default List<Vendor_Product_Image> mapImages(List<MultipartFile> files, @Context Product product, @Context User vendor) {
        return files.stream()
                .map(file -> mapMultipartFileToProductImage(file, product, vendor))
                .collect(Collectors.toList());
    }

}
