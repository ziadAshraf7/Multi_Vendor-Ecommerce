package com.example.ecommerce_app.Services.ProductAttributeValue;

import com.example.ecommerce_app.Dto.ProductAttributeValueTable.ProductAttributeValueCreationDto;
import com.example.ecommerce_app.Dto.ProductAttributeValueTable.ProductAttributeValueRemovalDto;
import com.example.ecommerce_app.Entity.Attribute;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.ProductAttributeValue;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabaseInternalServerError;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.ProductAttributeValue.ProductAttributeValueRepository;
import com.example.ecommerce_app.Services.Attribute.AttributeService;
import com.example.ecommerce_app.Services.Product.ProductService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class ProductAttributeValueServiceImp
        implements ProductAttributeValueService{

    private ProductService productService;

    private AttributeService attributeService;

    private ProductAttributeValueRepository productAttributeValueRepository;

    @Transactional
    @Override
    public void addProductAttributeValue(ProductAttributeValueCreationDto productAttributeValueCreationDto) {

        try {
            Product product = productService.getProductEntityById(productAttributeValueCreationDto.getProductId());
            Attribute attribute = attributeService.getAttributeEntityById(productAttributeValueCreationDto.getAttributeId());

            ProductAttributeValue productAttributeValue = ProductAttributeValue
                    .builder()
                    .product(product)
                    .attribute(attribute)
                    .value(productAttributeValueCreationDto.getValue())
                    .build();

            productAttributeValueRepository.save(productAttributeValue);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Database error while persisting new attribute value ");
        }

    }

    @Transactional
    @Override
    public void removeProductAttributeValue(ProductAttributeValueRemovalDto productAttributeValueRemovalDto) {

        Product product = productService.getProductEntityById(productAttributeValueRemovalDto.getProductId());
        Attribute attribute = attributeService.getAttributeEntityById(productAttributeValueRemovalDto.getAttributeId());
        ProductAttributeValue productAttributeValue = productAttributeValueRepository.findById(
                productAttributeValueRemovalDto.getValueId()
        ).orElseThrow(() -> new CustomNotFoundException("Cannot find attribute value "));


        try {
            productAttributeValueRepository.deleteByProductIdAndAttributeIdAndValueId(
                    product.getId() ,
                    attribute.getId() ,
                    productAttributeValue.getId()
            );
        }catch (DatabaseInternalServerError e){
            log.error(e.getMessage());
            throw new DatabaseInternalServerError("Database error  Unable to remove attribute value");
        }


    }


    @Override
    public Map<String , List<ProductAttributeValue>> getProductAttributeValues(long productId) {
        List<ProductAttributeValue> productAttributeValues = productAttributeValueRepository.findByProductId(productId);


       Map<String , List<ProductAttributeValue>> map = new HashMap<>();

       for(ProductAttributeValue productAttributeValue : productAttributeValues){
           map.compute(productAttributeValue.getAttribute().getName() , (key , value) -> {
               if(value == null) return new ArrayList<>(List.of(productAttributeValue));
               value.add(productAttributeValue);
               return value;
           });
       }
       return map;
    }
}
