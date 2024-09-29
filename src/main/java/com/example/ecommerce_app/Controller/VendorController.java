package com.example.ecommerce_app.Controller;


import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Update_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Image_Dto;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.Product_Mangement.Product_Management_Service;
import com.example.ecommerce_app.Services.Vendor_Product_Image.Vendor_Product_Image_Service;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("api/vendor")
@AllArgsConstructor
public class VendorController {

    private final ProductService productService;

    private final Vendor_Product_Image_Service vendor_product_image_service;

    private final Product_Management_Service productManagementService;

    @PostMapping("/product")
    public ResponseEntity<String> addProduct(@ModelAttribute Product_Creation_Dto product_creation_dto){
            productManagementService.addProduct(product_creation_dto);
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


    @DeleteMapping("/product/images")
    public ResponseEntity<String> deleteImages(@RequestParam Long vendorId , @RequestParam Long productId ) throws IOException {
            vendor_product_image_service.removeAllImagesPerVendorProduct(vendorId , productId);
            return new ResponseEntity<>("deleted Successfully" , HttpStatus.OK);
      }

      @GetMapping("/test")
    public String get(){
        return "test";
      }
}
