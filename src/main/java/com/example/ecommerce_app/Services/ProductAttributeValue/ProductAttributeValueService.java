package com.example.ecommerce_app.Services.ProductAttributeValue;

import com.example.ecommerce_app.Dto.ProductAttributeValueTable.ProductAttributeValueCreationDto;
import com.example.ecommerce_app.Dto.ProductAttributeValueTable.ProductAttributeValueRemovalDto;
import com.example.ecommerce_app.Entity.ProductAttributeValue;

import java.util.List;
import java.util.Map;

public interface ProductAttributeValueService {

    void addProductAttributeValue(ProductAttributeValueCreationDto ProductAttributeValueCreationDto);

    void removeProductAttributeValue(ProductAttributeValueRemovalDto productAttributeValueRemovalDto);

    Map<String , List<ProductAttributeValue>> getProductAttributeValues(long productId);

}
