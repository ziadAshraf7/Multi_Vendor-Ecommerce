package com.example.ecommerce_app.Services.AdminRequestsApproval;

import com.example.ecommerce_app.Dto.ProductRejectionRequest.ProductRejectionRequestDto;
import com.example.ecommerce_app.Dto.Product_Table.ProductCreationDto;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface AdminRequestsApprovalService {

    void rejectProduct(ProductRejectionRequestDto productRejectionRequestDto) throws JsonProcessingException;

    void acceptProduct(ProductCreationDto productCreationDto , String messageId) throws IOException;

}
