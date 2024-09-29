package com.example.ecommerce_app.Services.Product_Mangement;


import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Entity.Embedded_Ids.Vendor_Product_EmbeddedId;
import com.example.ecommerce_app.Mapper.ProductMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class Product_Management_Service_Imp  implements Product_Management_Service{

    private final BrandService brandService;

    private final CategoryService categoryService;

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final Vendor_Product_Repository vendor_product_repository;

    private final UserServiceImp userServiceImp;

    private final Vendor_Product_Image_Repository vendor_product_image_repository;

    @Override
    @Transactional
    public void addProduct(Product_Creation_Dto product_creation_dto) {

        try {
            Brand brand = brandService.getBrandEntityById(product_creation_dto.getBrandId());

            Category subCategory = categoryService.getSubCategoryEntityById(product_creation_dto.getSubCategoryId());

            User vendor = userServiceImp.getUserEntityById(product_creation_dto.getVendorId() , UserRoles.ROLE_VENDOR);

            Product product = Product.builder()
                    .description(product_creation_dto.getDescription())
                    .brand(brand)
                    .name(product_creation_dto.getName())
                    .rating(product_creation_dto.getRating())
                    .subCategory(subCategory)
                    .thumbNail(product_creation_dto.getThumbNail().getBytes())
                    .title(product_creation_dto.getTitle())
                    .build();

            Vendor_Product vendor_product = Vendor_Product.builder()
                    .id(new Vendor_Product_EmbeddedId())
                    .stock(product_creation_dto.getStock())
                    .price(UtilsClass.calcProductFinalPrice(product_creation_dto.getPrice() , product_creation_dto.getDiscount()))
                    .discount(product_creation_dto.getDiscount())
                    .vendor(vendor)
                    .product(product)
                    .build();

            Vendor_Product vendorProduct = vendor_product_repository.save(vendor_product);

            Product newPersistedProduct = vendorProduct.getProduct();

            List<Vendor_Product_Image> vendor_product_imageList = new ArrayList<>(product_creation_dto.getImageFiles().size());

            for(MultipartFile imageFile : product_creation_dto.getImageFiles())
                vendor_product_imageList.add( Vendor_Product_Image.builder()
                        .product(newPersistedProduct)
                        .image(imageFile.getBytes())
                        .build());

            vendor_product_image_repository.saveAll(vendor_product_imageList);

        }catch (RuntimeException | IOException e){
            throw new RuntimeException(e);
        }

    }
}
