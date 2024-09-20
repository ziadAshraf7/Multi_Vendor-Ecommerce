package com.example.ecommerce_app.Services.Interfaces;


import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Image_Dto;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.Vendor_Product.Vendor_Product_Service;
import com.example.ecommerce_app.Services.Vendor_Product_Image.Vendor_Product_Image_Service;

public interface Product_Management_Service extends
        ProductService,
        Product_Query_Service ,
        Vendor_Product_Service,
        Vendor_Product_Image_Service
{


    void ManageVendorProduct(
            Product_Creation_Dto productCreationDto ,
            Vendor_Product_Image_Dto vendorProductImageDto ,
            Vendor_Product_Creation_Dto vendorProductDto);




}
