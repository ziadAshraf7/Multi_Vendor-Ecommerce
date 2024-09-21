package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Dto.Brand_Table.BrandCreationDto;
import com.example.ecommerce_app.Dto.Category_Table.Parent_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.Category_Table.Sub_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Creation_Dto;
import com.example.ecommerce_app.Services.Brand.BrandServiceImp;
import com.example.ecommerce_app.Services.Category.CategoryServiceImp;
import com.example.ecommerce_app.Services.Vendor_Product.Vendor_Product_Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private final BrandServiceImp brandService;

    private final CategoryServiceImp categoryServiceImp;

    private final Vendor_Product_Service vendor_product_service;


    @PostMapping("/brand")
    public ResponseEntity<BrandCreationDto> addBrand(@ModelAttribute BrandCreationDto brandCreationDto){
        brandService.addBrand(brandCreationDto);
        return new ResponseEntity<>(brandCreationDto , HttpStatus.CREATED) ;
    }

    @PostMapping("/category")
    public ResponseEntity<Parent_Category_Creation_Dto> addParentCategory(@ModelAttribute Parent_Category_Creation_Dto parentCategoryCreationDto){
        categoryServiceImp.addParentCategory(parentCategoryCreationDto);
        return new ResponseEntity<>( parentCategoryCreationDto, HttpStatus.CREATED);
      }

    @PostMapping("/subCategory")
    public ResponseEntity<Sub_Category_Creation_Dto> addSubCategory(@ModelAttribute Sub_Category_Creation_Dto subCategoryCreationDto){
        categoryServiceImp.addSubCategory(subCategoryCreationDto);
        return new ResponseEntity<>( subCategoryCreationDto, HttpStatus.CREATED);
    }

    @PostMapping("/linkVendorWithProduct")
    public ResponseEntity<String> link(@ModelAttribute Vendor_Product_Creation_Dto vendor_product_creation_dto){
            vendor_product_service.link_vendor_with_product(vendor_product_creation_dto);
          return   new ResponseEntity<>("Product is linked with Vendor Successfully" , HttpStatus.CREATED);
      }


}
