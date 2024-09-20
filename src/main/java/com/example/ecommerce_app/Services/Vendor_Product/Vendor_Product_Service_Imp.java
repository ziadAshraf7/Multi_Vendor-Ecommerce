package com.example.ecommerce_app.Services.Vendor_Product;

import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Overview_Dto;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.Vendor_Product;
import com.example.ecommerce_app.Mapper.Vendor_Product_Mapper;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.Vendor_Product_Repository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Data
@Service
public class Vendor_Product_Service_Imp implements Vendor_Product_Service {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private Vendor_Product_Mapper vendor_product_mapper;

    private Vendor_Product_Repository vendor_product_repository;


    @Override
    public Vendor_Product_Overview_Dto link_vendor_with_product(Vendor_Product_Creation_Dto vendor_product_creation_dto)  {

        Product product = productRepository.findById(vendor_product_creation_dto.getProductId())
                .orElseThrow(() -> new RuntimeException("product is not found"));

        User vendor = userRepository.findById(vendor_product_creation_dto.getVendorId())
                .orElseThrow(() -> new RuntimeException("vendor is not found"));

        Vendor_Product vendorProduct = vendor_product_mapper.
                toEntity(vendor_product_creation_dto );

        vendorProduct.setProduct(product);
        vendorProduct.setVendor(vendor);

        try {
            vendor_product_repository.save(vendorProduct);
            return vendor_product_mapper.to_Vendor_Product_Overview_Dto(vendorProduct);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }    }

    @Override
    public void delete_vendor_product(long vendorId, long productId) {
        try {
            vendor_product_repository.delete_vendor_product(vendorId , productId);
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
