package com.example.ecommerce_app.Services.ProductManagement;


import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Attribute.AttributeRepository;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.ProductAttributeValue.ProductAttributeValueRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product_Image.Vendor_Product_Image_Repository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Brand.BrandService;
import com.example.ecommerce_app.Services.Category.CategoryService;
import com.example.ecommerce_app.Services.User.UserServiceImp;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import com.example.ecommerce_app.Utills.UtilsClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Data
@AllArgsConstructor
@Slf4j
public class ProductManagementServiceImp implements ProductManagementService {

    private final BrandService brandService;

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final VendorProductRepository vendor_product_repository;

    private final UserServiceImp userServiceImp;

    private final Vendor_Product_Image_Repository vendor_product_image_repository;

    private final CategoryService categoryService;

    private final ProductAttributeValueRepository productAttributeValueRepository;

    private final AttributeRepository attributeRepository;

    @Override
    @Transactional
    public void addProduct(Product_Creation_Dto productCreationDto) throws IOException {

            Brand brandReference = brandService.getBrandEntityById(productCreationDto.getBrandId());

            Category subCategoryReference = categoryService.getSubCategoryEntityById(productCreationDto.getSubCategoryId());

            User vendorReference = userServiceImp.getUserEntityById(productCreationDto.getVendorId() , UserRoles.ROLE_VENDOR);

            Product existingProduct = productRepository.findByName(productCreationDto.getName());

            if(existingProduct != null) throw new CustomConflictException("product " + productCreationDto.getName() + " is already exists");

            long productId = saveProductEntity(productCreationDto , brandReference , subCategoryReference);

            Product productReference = productRepository.getReferenceById(productId);

            addAttributesWithValuesToProduct(productCreationDto.getProductAttributesWithValues() , productReference);

            saveVendorProductEntity(productCreationDto , productReference , vendorReference);

            saveProductImages(productCreationDto , productReference , vendorReference);

    }

    @Transactional
    private long saveProductEntity(Product_Creation_Dto product_creation_dto ,
                                   Brand brandReference ,
                                   Category subCategoryReference
                                   ) throws IOException {

            Product product = productRepository.save(Product.builder()
                    .description(product_creation_dto.getDescription())
                    .brand(brandReference)
                    .name(product_creation_dto.getName().toLowerCase())
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
            Product_Creation_Dto productCreationDto ,
            Product productReference ,
            User vendorReference
            ){
            VendorProduct vendor_product = VendorProduct.builder()
                    .stock(productCreationDto.getStock())
                    .price(UtilsClass.calcProductFinalPrice(productCreationDto.getPrice() , productCreationDto.getDiscount()))
                    .discount(productCreationDto.getDiscount())
                    .vendor(vendorReference)
                    .product(productReference)
                    .build();

        try {
             vendor_product_repository.save(vendor_product);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to link the product with the vendor");
        }

    }

    @Transactional
    private void saveProductImages(Product_Creation_Dto productCreationDto ,
                                   Product productReference ,
                                   User vendor
                                   ) throws IOException {

            List<Vendor_Product_Image> vendor_product_imageList = new ArrayList<>(productCreationDto.getImageFiles().size());

            for(MultipartFile imageFile : productCreationDto.getImageFiles())
                vendor_product_imageList.add( Vendor_Product_Image.builder()
                        .vendor(vendor)
                        .product(productReference)
                        .image(imageFile.getBytes())
                        .build());

        try {
            vendor_product_image_repository.saveAll(vendor_product_imageList);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to add Images for the Product");
        }


    }

    @Transactional
    private void addAttributesWithValuesToProduct(
            Map<Long, List<String>> attrValues ,
            Product productReference
            ){

        List<ProductAttributeValue> productAttributeValues = new ArrayList<>();

        for(Map.Entry<Long , List<String>> entry : attrValues.entrySet()){

            Attribute attribute = attributeRepository.findById(entry.getKey())
                    .orElseThrow(() -> new CustomNotFoundException("attribute is not found"));
            System.out.println(attribute.getSubCategories().size());
            System.out.println("size");

            if(!attribute.getSubCategories().stream().map(Category::getId).toList()
                    .contains(productReference.getSubCategory().getId())) throw new CustomConflictException("attribute is not part of product category");

            for(String value : entry.getValue()){
                productAttributeValues.add(
                        ProductAttributeValue
                                .builder()
                                .attribute(attribute)
                                .product(productReference)
                                .value(value)
                                .build()
                );
            }
        }

        try {
            productAttributeValueRepository.saveAll(productAttributeValues);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Attributes Entered are not Found");
        }
    }
}