package com.example.ecommerce_app.Services.PendingProductsApproval;

import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface PendingProductApprovalService {

    void rejectProduct(String vendorId, String productName) throws JsonProcessingException;

    void acceptProduct(Product_Creation_Dto product_creation_dto) throws JsonProcessingException;

}
