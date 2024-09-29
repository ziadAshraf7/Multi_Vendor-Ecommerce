package com.example.ecommerce_app.Services.Vendor_Product;

import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Overview_Dto;
import com.example.ecommerce_app.Entity.Vendor_Product;

public interface Vendor_Product_Service {

    Vendor_Product_Overview_Dto link_vendor_with_product(Vendor_Product_Creation_Dto vendor_product_creation_dto )  ;

    void delete_vendor_product(long vendorId , long productId);

    Vendor_Product getByVendorIdAndProductId(long vendorId , long productId);

}
