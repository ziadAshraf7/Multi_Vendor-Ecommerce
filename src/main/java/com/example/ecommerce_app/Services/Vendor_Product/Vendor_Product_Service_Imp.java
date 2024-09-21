package com.example.ecommerce_app.Services.Vendor_Product;

import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Overview_Dto;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.Vendor_Product;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Mapper.Vendor_Product_Mapper;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.Vendor_Product_Repository;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.User.UserService;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Data
@Service
public class Vendor_Product_Service_Imp implements Vendor_Product_Service {

    private static final Logger log = LoggerFactory.getLogger(Vendor_Product_Service_Imp.class);

    private Vendor_Product_Mapper vendor_product_mapper;

    private Vendor_Product_Repository vendor_product_repository;

    private final ProductService productService;

    private final UserService userService;

    @Override
    @Transactional
    public Vendor_Product_Overview_Dto link_vendor_with_product(Vendor_Product_Creation_Dto vendor_product_creation_dto)  {

        Product product = productService.getProductEntityById(vendor_product_creation_dto.getProductId());

        User vendor = userService.getUserEntityById(vendor_product_creation_dto.getVendorId() , UserRoles.VENDOR);

        Vendor_Product vendorProduct = vendor_product_mapper. toEntity(vendor_product_creation_dto );

        vendorProduct.setProduct(product);
        vendorProduct.setVendor(vendor);

        try {
            vendor_product_repository.save(vendorProduct);
            return vendor_product_mapper.to_Vendor_Product_Overview_Dto(vendorProduct);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to link Vendor with Product");
        }
    }

    @Override
    @Transactional
    public void delete_vendor_product(long vendorId, long productId) {
        try {
            vendor_product_repository.delete_vendor_product(vendorId , productId);
        }catch (RuntimeException e){
            throw new CustomRuntimeException("Unable to unlink the vendor from the product");
        }
    }
}
