package com.example.ecommerce_app.Services.Attribute;

import com.example.ecommerce_app.Dto.Attribute_Table.AttributeCreationDto;
import com.example.ecommerce_app.Dto.Attribute_Table.AttributeDto;
import com.example.ecommerce_app.Dto.Attribute_Table.AttributeUpdateDto;
import com.example.ecommerce_app.Entity.Attribute;
import com.example.ecommerce_app.Entity.Category;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabaseInternalServerError;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Attribute.AttributeRepository;
import com.example.ecommerce_app.Services.Category.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class AttributeServiceImp implements AttributeService{

    private final AttributeRepository attributeRepository;

    private final CategoryService categoryService;

    @Transactional
    @Override
    public void addAttribute(AttributeCreationDto attributeCreationDto) {

            Attribute attribute = attributeRepository.getEntityByName(attributeCreationDto.getAttributeName().toLowerCase());
            if(attribute != null) throw new CustomConflictException("attribute name is already exists");
            Attribute newAttribute =  Attribute.builder()
                    .name(attributeCreationDto.getAttributeName().toLowerCase()).build();

        try {
            attributeRepository.save(newAttribute);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Database error while creating new attribute");
        }
    }

    @Transactional
    @Override
    public void deleteAttributeById(long attributeId) {
        Attribute attribute = getAttributeEntityById(attributeId);
        if(attribute == null) throw new CustomNotFoundException("attribute id " + attributeId + " is not found");
        try {
            attributeRepository.deleteById(attribute.getId());
        }catch (DatabaseInternalServerError e){
            log.error(e.getMessage());
            throw new DatabaseInternalServerError("Database Error while deleting attribute");
        }
    }

    @Transactional
    @Override
    public void deleteAttributeByName(String name) {

            Attribute attribute = attributeRepository.getEntityByName(name);
            if(attribute == null) throw new CustomNotFoundException("attribute name " + name + " is not found");
        try {
            attributeRepository.deleteEntityByName(attribute.getName());
        }catch (DatabaseInternalServerError e){
            log.error(e.getMessage());
            throw new DatabaseInternalServerError("Database error while deleting attribute");
        }

    }

    @Transactional
    @Override
    public void updateAttribute(AttributeUpdateDto attributeUpdateDto) {
        Attribute attribute = getAttributeEntityById(attributeUpdateDto.getAttributeId());
        if(attribute == null) throw new CustomNotFoundException("attribute id " + attributeUpdateDto.getAttributeId() + " is not found");

        if(attributeUpdateDto.getNewName() != null) attribute.setName(attributeUpdateDto.getNewName());

        if(attributeUpdateDto.getNewSubCategoryId() != null){
            Category subCategory = categoryService.getSubCategoryEntityById(attributeUpdateDto.getNewSubCategoryId());

            attribute.addNewSubCategory(subCategory);
        }

        try {
            attributeRepository.save(attribute);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("error while updating attribute entity");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Attribute getAttributeEntityById(long id) {
        return  attributeRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Attribute not found with attribute id " + id));
    }

    @Transactional(readOnly = true)
    @Override
    public Attribute getAttributeEntityByName(String name) {
        Attribute attribute = attributeRepository.getEntityByName(name);

        if(attribute == null) throw new CustomNotFoundException("Cannot find attribute with name " + name);

        return attribute;

    }

    @Transactional(readOnly = true)
    @Override
    public List<AttributeDto> getSubCategoryAttributes(long categoryId) {

        List<Attribute> attributes = attributeRepository.findBySubCategoryId(categoryId);

        List<AttributeDto> attributeDtos = new ArrayList<>(attributes.size());

        for(Attribute attribute : attributes){
            attributeDtos.add(AttributeDto.builder()
                            .name(attribute.getName())
                            .attributeId(attribute.getId())
                    .build());
        }

        return attributeDtos;
    }
}
