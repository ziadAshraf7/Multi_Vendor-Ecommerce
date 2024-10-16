package com.example.ecommerce_app.Services.Product_Mangement;


import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Entity.Embedded_Ids.Vendor_Product_EmbeddedId;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Exceptions.Exceptions.ImageNumberExceededException;
import com.example.ecommerce_app.Mapper.ProductMapper;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product_Image.Vendor_Product_Image_Repository;
import com.example.ecommerce_app.Repositery.Vendor_Product.Vendor_Product_Repository;
import com.example.ecommerce_app.Services.Brand.BrandService;
import com.example.ecommerce_app.Services.Category.CategoryService;
import com.example.ecommerce_app.Services.User.UserServiceImp;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import com.example.ecommerce_app.Utills.UtilsClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
@Slf4j
public class ProductManagementServiceImp implements Product_Management_Service{

    private final BrandService brandService;

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final Vendor_Product_Repository vendor_product_repository;

    private final UserServiceImp userServiceImp;

    private final Vendor_Product_Image_Repository vendor_product_image_repository;

    private final CategoryService categoryService;

    @Override
    @Transactional
    public void addProduct(Product_Creation_Dto product_creation_dto) {

        try {
            Brand brandReference = brandService.getBrandEntityById(product_creation_dto.getBrandId());

            Category subCategoryReference = categoryService.getSubCategoryEntityById(product_creation_dto.getSubCategoryId());

            User vendorReference = userServiceImp.getUserEntityById(product_creation_dto.getVendorId() , UserRoles.ROLE_VENDOR);

            long productId = saveProductEntity(product_creation_dto , brandReference , subCategoryReference);

            Product productReference = productRepository.getReferenceById(productId);

            saveVendorProductEntity(product_creation_dto , productReference , vendorReference);

            saveProductImages(product_creation_dto , productReference);

        }catch (CustomRuntimeException | IOException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to add new Product");
        }

    }

    @Transactional
    private long saveProductEntity(Product_Creation_Dto product_creation_dto ,
                                   Brand brandReference ,
                                   Category subCategoryReference
                                   ) throws IOException {

            Product product = productRepository.save(Product.builder()
                    .description(product_creation_dto.getDescription())
                    .brand(brandReference)
                    .name(product_creation_dto.getName())
                    .rating(product_creation_dto.getRating())
                    .subCategory(subCategoryReference)
                    .thumbNail(product_creation_dto.getThumbNail().getBytes())
                    .title(product_creation_dto.getTitle())
                    .build());

            if (product.getId() == null) throw new DatabasePersistenceException("Unable to persist Product in Database " );

            return product.getId();

    }

    @Transactional
    private void saveVendorProductEntity(
            Product_Creation_Dto product_creation_dto ,
            Product productReference ,
            User vendorReference
            ){
        try {
            Vendor_Product vendor_product = Vendor_Product.builder()
                    .id(new Vendor_Product_EmbeddedId())
                    .stock(product_creation_dto.getStock())
                    .price(UtilsClass.calcProductFinalPrice(product_creation_dto.getPrice() , product_creation_dto.getDiscount()))
                    .discount(product_creation_dto.getDiscount())
                    .vendor(vendorReference)
                    .product(productReference)
                    .build();

             vendor_product_repository.save(vendor_product);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to link the product with the vendor");
        }

    }

    @Transactional
    private void saveProductImages(Product_Creation_Dto product_creation_dto ,
                                   Product productReference
                                   ) throws IOException {
        try {

            List<Vendor_Product_Image> vendor_product_imageList = new ArrayList<>(product_creation_dto.getImageFiles().size());

            for(MultipartFile imageFile : product_creation_dto.getImageFiles())
                vendor_product_imageList.add( Vendor_Product_Image.builder()
                        .product(productReference)
                        .image(imageFile.getBytes())
                        .build());

            vendor_product_image_repository.saveAll(vendor_product_imageList);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to add Images for the Product");
        }


    }

}
