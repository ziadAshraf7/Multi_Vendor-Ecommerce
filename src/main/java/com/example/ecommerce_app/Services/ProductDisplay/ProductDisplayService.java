package com.example.ecommerce_app.Services.ProductDisplay;

import com.example.ecommerce_app.Dto.Product_Table.ProductDetailedDto;

public interface ProductDisplayService {

    ProductDetailedDto getProduct(long productId);

}
