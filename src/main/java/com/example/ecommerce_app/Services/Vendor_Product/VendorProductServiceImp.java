package com.example.ecommerce_app.Services.Vendor_Product;

import com.example.ecommerce_app.Dto.VendorProductTable.VendorProductDeleteDto;
import com.example.ecommerce_app.Dto.VendorProductTable.VendorProductCreationDto;
import com.example.ecommerce_app.Dto.VendorProductTable.VendorProductOverviewDto;
import com.example.ecommerce_app.Dto.VendorProductTable.VendorProductUpdateDto;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.VendorProduct;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Mapper.Vendor_Product_Mapper;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.User.UserService;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Data
@Service
public class VendorProductServiceImp implements VendorProductService {

    private static final Logger log = LoggerFactory.getLogger(VendorProductServiceImp.class);

    private Vendor_Product_Mapper vendor_product_mapper;

    private VendorProductRepository vendorProductRepository;

    private final ProductService productService;

    private final UserService userService;

    @Transactional
    @Override
    public void updateProductPerVendorPrice(VendorProductUpdateDto vendorProductUpdateDto) {
        VendorProduct vendorProduct = vendorProductRepository.findById(vendorProductUpdateDto.getVendorProductId())
                .orElseThrow(() -> new CustomNotFoundException("vendor product is not found"));

        if(vendorProductUpdateDto.getPrice() <= 0 ) throw new CustomBadRequestException("Price cannot be less than 0");

        vendorProduct.setPrice(vendorProductUpdateDto.getPrice());
        vendorProductRepository.save(vendorProduct);

    }

    @Override
    @Transactional
    public VendorProductOverviewDto linkVendorWithProduct(VendorProductCreationDto vendor_product_creation_dto)  {

        Product product = productService.getProductEntityById(vendor_product_creation_dto.getProductId());

        User vendor = userService.getUserEntityById(vendor_product_creation_dto.getVendorId() , UserRoles.ROLE_VENDOR);

        VendorProduct vendorProduct = vendor_product_mapper.toEntity(vendor_product_creation_dto );

        vendorProduct.setProduct(product);
        vendorProduct.setVendor(vendor);

            vendorProductRepository.save(vendorProduct);
            return vendor_product_mapper.toVendorProductOverviewDto(vendorProduct);

    }

    @Override
    @Transactional
    public void deleteVendorProduct(VendorProductDeleteDto vendorProductDeleteDto) {
            VendorProduct vendorProduct = vendorProductRepository.findByVendorIdAndProductId(
                    vendorProductDeleteDto.getVendorId(),
                    vendorProductDeleteDto.getProductId()
            );
            if(vendorProduct == null) throw new CustomNotFoundException("vendor product is not found");
            vendorProductRepository.deleteVendorProduct(vendorProductDeleteDto.getVendorId() , vendorProductDeleteDto.getProductId());

    }

    @Override
    @Transactional(readOnly = true)
    public VendorProduct getByVendorIdAndProductId(long vendorProductId) {
            VendorProduct vendorProduct = vendorProductRepository.findByVendorProductId(vendorProductId);
            if(vendorProduct == null) throw new CustomNotFoundException("vendorProduct entity cannot be found");
            return  vendorProduct;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VendorProductOverviewDto> getVendorProducts(long vendorId , Pageable pageable) {
        Page<VendorProduct> vendorProducts = vendorProductRepository.findByVendorId(vendorId , pageable);
        return vendorProducts.map(vendor_product_mapper::toVendorProductOverviewDto);
    }



}
