package com.example.ecommerce_app.Services.Vendor_Product;

import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Dto.VendorProductTable.*;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Entity.VendorProduct;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabaseInternalServerError;
import com.example.ecommerce_app.Mapper.Vendor_Product_Mapper;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.User.UserService;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerErrorException;

import java.util.Objects;


@AllArgsConstructor
@Data
@Service
public class VendorProductServiceImp implements VendorProductService {

    private static final Logger log = LoggerFactory.getLogger(VendorProductServiceImp.class);

    private Vendor_Product_Mapper vendorProductMapper;

    private VendorProductRepository vendorProductRepository;

    private final ProductService productService;

    private final UserService userService;

    @Transactional
    @Override
    public void updateProductPerVendorPrice(VendorProductUpdateDto vendorProductUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        long authenticatedVendorId = ((AuthenticatedUserDto) authentication.getPrincipal()).getId();

        VendorProduct vendorProduct = vendorProductRepository.findById(vendorProductUpdateDto.getVendorProductId())
                .orElseThrow(() -> new CustomNotFoundException("vendor product is not found"));

        if(vendorProduct.getVendor().getId() != authenticatedVendorId) throw new CustomBadRequestException("user is not authorized");
        if(vendorProductUpdateDto.getPrice() <= 0 ) throw new CustomBadRequestException("Price cannot be less than 0");

        vendorProduct.setPrice(vendorProductUpdateDto.getPrice());

        try {
            vendorProductRepository.save(vendorProduct);
        }catch (DatabaseInternalServerError e){
            throw new DatabaseInternalServerError("error while updating vendor product");
        }

    }

    @Override
    @Transactional
    public VendorProductOverviewDto linkVendorWithProduct(VendorProductCreationDto vendor_product_creation_dto)  {

        Product product = productService.getProductEntityById(vendor_product_creation_dto.getProductId());

        User vendor = userService.getUserEntityById(vendor_product_creation_dto.getVendorId() , UserRoles.ROLE_VENDOR);
        VendorProduct existingVendorProduct = vendorProductRepository.findByVendorIdAndProductId(
                vendor_product_creation_dto.getVendorId() ,
                vendor_product_creation_dto.getProductId()
        );
        if(existingVendorProduct != null)   throw new CustomBadRequestException("product is already linked with the vendor");

        VendorProduct vendorProduct = vendorProductMapper.toEntity(vendor_product_creation_dto );

        vendorProduct.setProduct(product);
        vendorProduct.setVendor(vendor);
        try {
            vendorProductRepository.save(vendorProduct);
            return vendorProductMapper.toVendorProductOverviewDto(vendorProduct);
        }catch (CustomBadRequestException e){
            throw new CustomBadRequestException("product is already linked with the vendor");
        }

    }

    @Override
    @Transactional
    public void deleteVendorProduct(VendorProductDeleteDto vendorProductDeleteDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        long authenticatedVendorId = ((AuthenticatedUserDto) authentication.getPrincipal()).getId();

        if(!Objects.equals(((AuthenticatedUserDto) authentication.getPrincipal()).getRole(), UserRoles.ROLE_ADMIN.toString())){
            if(authenticatedVendorId != vendorProductDeleteDto.getVendorId())
                throw new CustomBadRequestException("user is not authorized ");
        }

        VendorProduct vendorProduct = vendorProductRepository.findByVendorIdAndProductId(
                vendorProductDeleteDto.getVendorId(),
                vendorProductDeleteDto.getProductId()
        );

        if(vendorProduct == null) throw new CustomNotFoundException("vendor product is not found");

        try {
            vendorProductRepository.deleteVendorProduct(vendorProductDeleteDto.getVendorId() , vendorProductDeleteDto.getProductId());
        }catch (CustomBadRequestException e){
            throw new CustomBadRequestException("Error while unlinking vendor from product");
        }
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
        return vendorProducts.map(vendorProductMapper::toVendorProductOverviewDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VendorProductCategoryInfoDto> getVendorProductsByCategory(long vendorId , long categoryId , Pageable pageable) {
        return vendorProductRepository.
                findByVendorProductIdAndCategoryId(vendorId , categoryId , pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<VendorProductBrandInfoDto> getVendorProductsByBrand(long vendorId , long brandId , Pageable pageable) {
        return vendorProductRepository.
                findByVendorProductIdAndBrandId(vendorId , brandId , pageable);
    }


}
