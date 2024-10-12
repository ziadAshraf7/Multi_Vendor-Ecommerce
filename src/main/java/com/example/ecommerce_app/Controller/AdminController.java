package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Dto.Brand_Table.BrandCreationDto;
import com.example.ecommerce_app.Dto.Category_Table.Parent_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.Category_Table.Sub_Category_Creation_Dto;
import com.example.ecommerce_app.Dto.PendingProducts.PendingProductGeneralData;
import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Update_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Image_Dto;
import com.example.ecommerce_app.Redis.database.PendingProduct.PendingProductsService;
import com.example.ecommerce_app.Services.PendingProductsApproval.PendingProductApprovalService;
import com.example.ecommerce_app.Services.Brand.BrandServiceImp;
import com.example.ecommerce_app.Services.Category.CategoryServiceImp;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.Vendor_Product.Vendor_Product_Service;
import com.example.ecommerce_app.Services.Vendor_Product_Image.Vendor_Product_Image_Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private final BrandServiceImp brandService;

    private final CategoryServiceImp categoryServiceImp;

    private final Vendor_Product_Service vendor_product_service;

    private final ProductService productService;

    private final Vendor_Product_Image_Service vendor_product_image_service;

    private final PendingProductsService pendingProductsService;

    private final PendingProductApprovalService productApprovalService;

    @GetMapping("/pendingProducts")
    public ResponseEntity<List<Product_Creation_Dto>> getPendingProducts(){
        return new ResponseEntity<>(pendingProductsService.getAllData("10").getPendingProducts() , HttpStatus.OK);
    }

    @GetMapping("/pendingProduct")
    public ResponseEntity<Product_Creation_Dto> getPendingProduct(@Param("vendorId") String vendorId , @Param("productName") String productName){
        return new ResponseEntity<>((Product_Creation_Dto) pendingProductsService.getData("10" ,
                PendingProductGeneralData
                        .builder()
                        .productName(productName)
                        .vendorId(vendorId)
                        .build()) ,
                HttpStatus.OK);
    }

    @PostMapping("/product")
    public ResponseEntity<String> acceptPendingProductRequest(@ModelAttribute Product_Creation_Dto product_creation_dto) throws JsonProcessingException {
        productApprovalService.acceptProduct(product_creation_dto);
        return new ResponseEntity<>("Created Successfully" , HttpStatus.CREATED);
    }

    @PutMapping("/product")
    public ResponseEntity<String> updateProduct(@ModelAttribute Product_Update_Dto product_update_dto){
        productService.updateProduct(product_update_dto);
        return new ResponseEntity<>("Created Successfully" , HttpStatus.OK);
    }


    @PostMapping("/product/image")
    public ResponseEntity<String> addImage(@ModelAttribute Vendor_Product_Image_Dto vendor_product_image_dto) throws IOException {
        vendor_product_image_service.addProductImage(vendor_product_image_dto);
        return new ResponseEntity<>("Created Successfully" , HttpStatus.CREATED);
    }

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
    public ResponseEntity<String> linkProductWithVendor(@ModelAttribute Vendor_Product_Creation_Dto vendor_product_creation_dto){
            vendor_product_service.link_vendor_with_product(vendor_product_creation_dto);
          return   new ResponseEntity<>("Product is linked with Vendor Successfully" , HttpStatus.CREATED);
      }


}
