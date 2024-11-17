package com.example.ecommerce_app.Services.Attribute;

import com.example.ecommerce_app.Dto.Attribute_Table.AttributeCreationDto;
import com.example.ecommerce_app.Dto.Attribute_Table.AttributeDto;
import com.example.ecommerce_app.Dto.Attribute_Table.AttributeUpdateDto;
import com.example.ecommerce_app.Entity.Attribute;

import java.util.List;

public interface AttributeService {

    void addAttribute(AttributeCreationDto attributeCreationDto);

    void deleteAttributeById(long attributeId);

    void deleteAttributeByName(String name);

    void updateAttribute(AttributeUpdateDto attributeUpdateDto);

    Attribute getAttributeEntityById(long id);

    Attribute getAttributeEntityByName(String name);

    List<AttributeDto> getSubCategoryAttributes(long categoryId);
}
