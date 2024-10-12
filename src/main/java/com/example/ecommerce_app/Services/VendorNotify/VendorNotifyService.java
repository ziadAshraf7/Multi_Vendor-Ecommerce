package com.example.ecommerce_app.Services.VendorNotify;

import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;

public interface VendorNotifyService {


    void sendProductCreationRequestToAdmin(Product_Creation_Dto product_creation_dto);

}
