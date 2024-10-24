package com.example.ecommerce_app.Services.Attribute;

import com.example.ecommerce_app.Dto.Attribute_Table.AttributeCreationDto;
import com.example.ecommerce_app.Entity.Attribute;

public interface AttributeService {

    void addAttribute(AttributeCreationDto attributeCreationDto);

    void deleteAttributeById(long attributeId);

    void deleteAttributeByName(String name);

    Attribute getAttributeEntityById(long id);

    Attribute getAttributeEntityByName(String name);
}
