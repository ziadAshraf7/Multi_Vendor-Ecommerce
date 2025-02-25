package com.example.ecommerce_app.Services.ProductManagement;


import com.example.ecommerce_app.Dto.Product_Table.ProductCreationDto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Projections.Category.CategoryIdsGeneralInfoView;
import com.example.ecommerce_app.Projections.User.UserGeneralInfoView;
import com.example.ecommerce_app.Repositery.Attribute.AttributeRepository;
import com.example.ecommerce_app.Repositery.Brand.BrandRepository;
import com.example.ecommerce_app.Repositery.Category.CategoryRepository;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.ProductAttributeValue.ProductAttributeValueRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product_Image.vendorProductImageRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Brand.BrandService;
import com.example.ecommerce_app.Services.Category.CategoryService;
import com.example.ecommerce_app.Services.FileSystemStorage.FileSystemStorageService;
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
import java.util.Objects;

@Service
@Data
@AllArgsConstructor
@Slf4j
public class ProductManagementServiceImp implements ProductManagementService {

    private final BrandService brandService;

    private final ProductRepository productRepository;

    private final VendorProductRepository vendorProductRepository;

    private final UserServiceImp userServiceImp;

    private final vendorProductImageRepository vendor_product_image_repository;

    private final CategoryService categoryService;

    private final ProductAttributeValueRepository productAttributeValueRepository;

    private final AttributeRepository attributeRepository;

    private final FileSystemStorageService fileSystemStorageService;

//    ------------------------------------------------

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void addProduct(ProductCreationDto productCreationDto) throws IOException {

            Brand brandReference = brandRepository.getReferenceById(productCreationDto.getBrandId());
            if(!brandRepository.existsById(productCreationDto.getBrandId())) throw new CustomNotFoundException("brand is not found");

            CategoryIdsGeneralInfoView subCategoryGeneralInfoEntity = categoryRepository.findGeneralInfo(
                    productCreationDto.getSubCategoryId()
            );

            if(subCategoryGeneralInfoEntity == null) throw new CustomNotFoundException("category is not found");
            if(subCategoryGeneralInfoEntity.getParentCategoryId() <= 0) throw new CustomBadRequestException("category is parent category");
            Category subCategoryReference = categoryRepository.getReferenceById(productCreationDto.getSubCategoryId());

            UserGeneralInfoView vendorGeneralInfoEntity = userRepository.findGeneralInfoById(productCreationDto.getVendorId());

            if(vendorGeneralInfoEntity == null ) throw new CustomNotFoundException("user vendor is not found");

            if(!Objects.equals(vendorGeneralInfoEntity.getUserRole(), UserRoles.ROLE_VENDOR))
                throw new CustomBadRequestException("User trying to add the product is not a vendor");

            User vendorReference = userRepository.getReferenceById(productCreationDto.getVendorId());

            boolean existingProductByName = productRepository.existsByName(productCreationDto.getName());

            if(existingProductByName) throw new CustomConflictException("product " + productCreationDto.getName() + " is already exists");

            long productId = saveProductEntity(productCreationDto , brandReference , subCategoryReference);

            Product productReference = productRepository.getReferenceById(productId);

            addAttributesWithValuesToProduct(productCreationDto.getProductAttributesWithValues() , productReference);

            saveVendorProductEntity(productCreationDto , productReference , vendorReference);

            saveProductImages(productCreationDto , productReference , vendorReference);

    }

    @Transactional
    private long saveProductEntity(ProductCreationDto product_creation_dto ,
                                   Brand brandReference ,
                                   Category subCategoryReference
                                   )  {

            Product product = productRepository.save(Product.builder()
                    .description(product_creation_dto.getDescription())
                    .brand(brandReference)
                    .name(product_creation_dto.getName().toLowerCase())
                    .rating(product_creation_dto.getRating())
                    .subCategory(subCategoryReference)
                    .thumbNail(product_creation_dto.getThumbNail().getOriginalFilename())
                    .title(product_creation_dto.getTitle())
                    .build());

            if (product.getId() == null) throw new DatabasePersistenceException("Unable to persist Product in Database " );

            return product.getId();

    }

    @Transactional
    private void saveVendorProductEntity(
            ProductCreationDto productCreationDto ,
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

             vendorProductRepository.save(vendor_product);

    }

    @Transactional
    private void saveProductImages(ProductCreationDto productCreationDto ,
                                   Product productReference ,
                                   User vendor
                                   ) throws IOException {

            List<vendorProductImage> vendor_product_imageList = new ArrayList<>(productCreationDto.getImageFiles().size());

            for(MultipartFile imageFile : productCreationDto.getImageFiles()){
                String imageFileName = fileSystemStorageService.saveImageToFileSystem(imageFile);
                vendor_product_imageList.add( vendorProductImage.builder()
                        .vendor(vendor)
                        .product(productReference)
                        .imageFileName(imageFileName)
                        .build());
            }

            vendor_product_image_repository.saveAll(vendor_product_imageList);

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

            productAttributeValueRepository.saveAll(productAttributeValues);

    }
}
