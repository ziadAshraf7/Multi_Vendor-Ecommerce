package com.example.ecommerce_app.Controller;
import com.example.ecommerce_app.Dto.Attribute_Table.AttributeCreationDto;
import com.example.ecommerce_app.Dto.Attribute_Table.AttributeUpdateDto;
import com.example.ecommerce_app.Dto.CategoryAttributeManagement.SubCategoryAttributeResponseDto;
import com.example.ecommerce_app.Dto.CategoryAttributeManagement.SubCategoryWithAttributeDto;
import com.example.ecommerce_app.Services.Attribute.AttributeService;
import com.example.ecommerce_app.Services.SubCategoryAttribute.CategoryAttributeManagementService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/attribute")
@AllArgsConstructor
public class AttributeController {

    private final AttributeService attributeService;

    private final CategoryAttributeManagementService categoryAttributeManagementService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addAttribute(@RequestBody() @Valid AttributeCreationDto attributeCreationDto){
        attributeService.addAttribute(attributeCreationDto);
        return new ResponseEntity<>("Created" , HttpStatus.CREATED);
    }

    @PostMapping("category/attribute/link")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> linkAttributeWithCategory(@RequestBody SubCategoryWithAttributeDto subCategoryWithAttributeDto){
        categoryAttributeManagementService.linkBetweenSubCategoryAndAttribute(subCategoryWithAttributeDto);
        return new ResponseEntity<>("Created" , HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateAttribute(@RequestBody AttributeUpdateDto attributeUpdateDto){
        attributeService.updateAttribute(attributeUpdateDto);
        return new ResponseEntity<>("Updated Successfully" , HttpStatus.CREATED);
    }


    @GetMapping("/category/{categoryId}")
    public ResponseEntity<SubCategoryAttributeResponseDto> getSubCategoryAttributes(
            @PathVariable("categoryId") long categoryId
    ){
        return ResponseEntity.ok(categoryAttributeManagementService.getSubCategoryAttributesNames(categoryId));
    }


}
