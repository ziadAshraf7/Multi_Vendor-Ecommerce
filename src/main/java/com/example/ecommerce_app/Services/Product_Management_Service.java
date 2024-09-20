package com.example.ecommerce_app.Services;

import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Detailed_Dto;

public interface Product_Management_Service {


    void addProduct(Product_Creation_Dto product_creation_dto);


}
