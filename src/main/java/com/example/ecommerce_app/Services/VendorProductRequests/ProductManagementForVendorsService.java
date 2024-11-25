package com.example.ecommerce_app.Services.VendorProductRequests;

import com.example.ecommerce_app.Dto.Product_Table.ProductCreationDto;

public interface ProductManagementForVendorsService {


    void sendProductCreationRequestToAdmin(ProductCreationDto product_creation_dto);

}
