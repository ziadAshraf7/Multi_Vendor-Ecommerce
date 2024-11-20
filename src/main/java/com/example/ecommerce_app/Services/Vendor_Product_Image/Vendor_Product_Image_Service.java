package com.example.ecommerce_app.Services.Vendor_Product_Image;

import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Image_Dto;

import java.io.IOException;
import java.util.List;

public interface Vendor_Product_Image_Service  {

    void addProductImage(Vendor_Product_Image_Dto productImageDto) throws IOException;

    void removeProductImage(long imageId);

    List<String> getVendorProductImages(long vendorId , long productId);


    void removeAllImagesPerVendorProduct(long vendorId , long productId);
}
