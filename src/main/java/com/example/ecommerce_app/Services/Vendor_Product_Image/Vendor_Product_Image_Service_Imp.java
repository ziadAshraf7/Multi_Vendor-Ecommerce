package com.example.ecommerce_app.Services.Vendor_Product_Image;

import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Image_Dto;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.Vendor_Product_Image;
import com.example.ecommerce_app.Mapper.Vendor_Product_Image_Mapper;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product_Image.Vendor_Product_Image_Repository;
import com.example.ecommerce_app.Services.User.UserServiceImp;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Service
public class Vendor_Product_Image_Service_Imp implements Vendor_Product_Image_Service {


    private final ProductRepository productRepository;

    private final UserServiceImp userServiceImp;

    private final Vendor_Product_Image_Mapper vendor_product_image_mapper;

    private final Vendor_Product_Image_Repository vendor_product_image_repository;


    @Override
    @Transactional
    public void addProductImage(Vendor_Product_Image_Dto vendor_product_image_dto) {

        long vendorId = vendor_product_image_dto.getVendorId();
        long productId = vendor_product_image_dto.getProductId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User vendor = userServiceImp.getUserEntity(vendorId , UserRoles.VENDOR);

        List<Vendor_Product_Image> images = vendor_product_image_mapper.toEntity(vendor_product_image_dto.getImageFiles(), product, vendor);

        try {
            vendor_product_image_repository.saveAll(images);
        }catch (RuntimeException e){
            throw new RuntimeException("");
        }

    }


    @Override
    public void removeProductImage(long imageId) {
        try {
            vendor_product_image_repository.deleteById(imageId);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<byte[]> getVendorProductImages(long vendorId, long productId) {
        List<Vendor_Product_Image> vendor_product_images = vendor_product_image_repository.getAllImagesPerVendorProduct(vendorId , productId);

        List<byte[]> images = new ArrayList<>(vendor_product_images.size());

        for(Vendor_Product_Image vendor_product_image : vendor_product_images){
            images.add(vendor_product_image.getImage());
        }
        return images;
    }

    @Override
    public void removeAllImagesPerVendorProduct(long vendorId, long productId) {
        try {
            vendor_product_image_repository.removeAllImagesPerVendorProduct(vendorId , productId);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
