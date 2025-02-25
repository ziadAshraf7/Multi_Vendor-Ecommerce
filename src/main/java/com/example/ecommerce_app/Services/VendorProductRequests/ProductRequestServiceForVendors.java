package com.example.ecommerce_app.Services.VendorProductRequests;

import com.example.ecommerce_app.Dto.Product_Table.ProductCreationDto;

public interface ProductRequestServiceForVendors {
    void sendProductCreationRequestToAdmin(ProductCreationDto productCreationDto);
}
