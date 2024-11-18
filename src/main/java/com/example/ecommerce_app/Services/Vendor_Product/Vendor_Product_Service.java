package com.example.ecommerce_app.Services.Vendor_Product;

import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.VendorProductOverviewDto;
import com.example.ecommerce_app.Entity.VendorProduct;

public interface Vendor_Product_Service {

    VendorProductOverviewDto link_vendor_with_product(Vendor_Product_Creation_Dto vendor_product_creation_dto )  ;

    void delete_vendor_product(long vendorId , long productId);

    VendorProduct getByVendorIdAndProductId(long vendorProductId);

}
