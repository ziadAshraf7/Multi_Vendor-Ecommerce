package com.example.ecommerce_app.Services.Vendor_Product_Image;

import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Image_Dto;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.vendorProductImage;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Mapper.VendorProductImageMapper;
import com.example.ecommerce_app.Repositery.Vendor_Product_Image.vendorProductImageRepository;
import com.example.ecommerce_app.Services.FileSystemStorage.FileSystemStorageService;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.User.UserService;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Service
@Slf4j
public class Vendor_Product_Image_Service_Imp implements Vendor_Product_Image_Service {


    private final ProductService productService;

    private final UserService userServiceImp;

    private final VendorProductImageMapper vendorProductImageMapper;

    private final vendorProductImageRepository vendorProductImageRepository;

    private final FileSystemStorageService fileSystemStorageService;

    @Override
    @Transactional
    public void addProductImage(Vendor_Product_Image_Dto vendor_product_image_dto) {

        long vendorId = vendor_product_image_dto.getVendorId();
        long productId = vendor_product_image_dto.getProductId();

        Product product = productService.getProductEntityById(productId);
        User vendor = userServiceImp.getUserEntityById(vendorId , UserRoles.ROLE_VENDOR);

        List<vendorProductImage> images = new ArrayList<>(vendor_product_image_dto.getImageFiles().size());



        vendor_product_image_dto.getImageFiles().forEach(imageFile -> {
            try {
                String imageFileName =  fileSystemStorageService.saveImageToFileSystem(imageFile);

                images.add(vendorProductImage.
                        builder()
                                .vendor(vendor)
                                .product(product)
                                .imageFileName(imageFileName)
                        .build());

            } catch (IOException e) {
                throw new CustomRuntimeException(e.getMessage());
            }
        });

        try {
            vendorProductImageRepository.saveAll(images);
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("unable to add images to the product id " + productId);
        }

    }


    @Override
    @Transactional
    public void removeProductImage(long imageId) {
        try {
            vendorProductImageRepository.deleteById(imageId);
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("unable to remove image");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getVendorProductImages(long vendorId, long productId) {
        try {
            List<vendorProductImage> vendor_product_images = vendorProductImageRepository.getAllImagesPerVendorProduct(vendorId , productId);

            List<String> images = new ArrayList<>(vendor_product_images.size());

            for(vendorProductImage vendor_product_image : vendor_product_images){
                images.add(vendor_product_image.getImageFileName());
            }

            return images;
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to retrieve product images");
        }

    }

    @Override
    @Transactional
    public void removeAllImagesPerVendorProduct(long vendorId, long productId) {
        try {
            vendorProductImageRepository.removeAllImagesPerVendorProduct(vendorId , productId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("unable to remove images");
        }
    }
}
