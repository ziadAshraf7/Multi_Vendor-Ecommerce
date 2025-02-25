package com.example.ecommerce_app.Services.Vendor_Product;

import com.example.ecommerce_app.Dto.VendorProductTable.*;
import com.example.ecommerce_app.Entity.VendorProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorProductService {

    void updateProductPerVendorPrice(VendorProductUpdateDto vendorProductUpdateDto);

    VendorProductOverviewDto linkVendorWithProduct(VendorProductCreationDto vendor_product_creation_dto )  ;

    void deleteVendorProduct(VendorProductDeleteDto vendorProductDeleteDto);

    VendorProduct getByVendorIdAndProductId(long vendorProductId);

    Page<VendorProductOverviewDto> getVendorProducts(long vendorId , Pageable pageable);

    Page<VendorProductCategoryInfoDto> getVendorProductsByCategory(long vendorId , long categoryId , Pageable pageable);

    Page<VendorProductBrandInfoDto> getVendorProductsByBrand(long vendorId  , long brandId , Pageable pageable);

}
