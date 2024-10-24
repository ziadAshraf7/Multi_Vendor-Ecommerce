package com.example.ecommerce_app.Services.Attribute;

import com.example.ecommerce_app.Dto.Attribute_Table.AttributeCreationDto;
import com.example.ecommerce_app.Entity.Attribute;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabaseInternalServerError;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Attribute.AttributeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class AttributeServiceImp implements AttributeService{

    private final AttributeRepository attributeRepository;

    @Transactional
    @Override
    public void addAttribute(AttributeCreationDto attributeCreationDto) {
        try {
            Attribute attribute =  Attribute.builder()
                    .name(attributeCreationDto.getAttributeName().toLowerCase()).build();

            attributeRepository.save(attribute);

        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Database error while creating new attribute");
        }
    }

    @Transactional
    @Override
    public void deleteAttributeById(long attributeId) {
        try {

            Attribute attribute = getAttributeEntityById(attributeId);

            attributeRepository.deleteById(attribute.getId());
        }catch (DatabaseInternalServerError e){
            log.error(e.getMessage());
            throw new DatabaseInternalServerError("Database Error while deleting attribute");
        }
    }

    @Transactional
    @Override
    public void deleteAttributeByName(String name) {

        try {

            Attribute attribute = attributeRepository.getEntityByName(name);

            attributeRepository.deleteEntityByName(attribute.getName());

        }catch (DatabaseInternalServerError e){
            log.error(e.getMessage());
            throw new DatabaseInternalServerError("Database error while deleting attribute");
        }





    }

    @Override
    public Attribute getAttributeEntityById(long id) {
        return  attributeRepository.findById(id)
                .orElseThrow(() -> new CustomNotFoundException("Attribute not found with attribute id " + id));
    }

    @Override
    public Attribute getAttributeEntityByName(String name) {
        Attribute attribute = attributeRepository.getEntityByName(name);

        if(attribute == null) throw new CustomNotFoundException("Cannot find attribute with name " + name);

        return attribute;

    }
}
