package com.example.ecommerce_app.Controller;


import com.example.ecommerce_app.Dto.Product_Table.Product_Creation_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Update_Dto;
import com.example.ecommerce_app.Dto.Vendor_Product_Table.Vendor_Product_Image_Dto;
import com.example.ecommerce_app.Services.VendorNotify.VendorNotifyService;
import com.example.ecommerce_app.Services.Vendor_Product_Image.Vendor_Product_Image_Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@RequestMapping("api/vendor")
@AllArgsConstructor
@Slf4j
public class VendorController {


    private final Vendor_Product_Image_Service vendor_product_image_service;

    private final VendorNotifyService vendorNotifyService;

    @PostMapping("/product")
    public ResponseEntity<String> productAddingRequestToAdmin(
            @ModelAttribute
            Product_Creation_Dto product_creation_dto) {

        vendorNotifyService.sendProductCreationRequestToAdmin(product_creation_dto);

        return new ResponseEntity<>(
                "Adding Product Request has been Successfully Sent" ,
                HttpStatus.OK);
    }


    @PutMapping("/product")
    public ResponseEntity<String> updateProduct(@ModelAttribute Product_Update_Dto product_update_dto){
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


}
