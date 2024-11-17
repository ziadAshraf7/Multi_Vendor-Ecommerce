package com.example.ecommerce_app.Services.ProductManagement;

import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;

import java.io.IOException;

public interface ProductManagementService {


    void addProduct(Product_Creation_Dto product_creation_dto) throws IOException;


}
