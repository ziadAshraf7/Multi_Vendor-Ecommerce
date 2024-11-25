package com.example.ecommerce_app.Services.ProductManagement;

import com.example.ecommerce_app.Dto.Product_Table.ProductCreationDto;

import java.io.IOException;

public interface ProductManagementService {


    void addProduct(ProductCreationDto product_creation_dto) throws IOException;


}
