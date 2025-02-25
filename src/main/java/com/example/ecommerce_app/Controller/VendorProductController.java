package com.example.ecommerce_app.Controller;
import com.example.ecommerce_app.Dto.VendorProductTable.*;
import com.example.ecommerce_app.Services.Vendor_Product.VendorProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendorProducts")
@AllArgsConstructor
public class VendorProductController {

    private final VendorProductService vendorProductService;

    @GetMapping
    public ResponseEntity<Page<VendorProductOverviewDto>> getProductsPerVendor(@RequestParam("vendorId") long vendorId , @RequestParam int pageNumber, @RequestParam int pageSize){
        PageRequest pageable = PageRequest.of(pageNumber , pageSize);
        return ResponseEntity.ok(vendorProductService.getVendorProducts(vendorId, pageable));
    }

    @PostMapping("/product/vendor/link")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> linkProductWithVendor(@RequestBody VendorProductCreationDto vendorProductCreationDto){
        vendorProductService.linkVendorWithProduct(vendorProductCreationDto);
        return   new ResponseEntity<>("Product is linked with Vendor Successfully" , HttpStatus.CREATED);
    }

    @DeleteMapping("/product/vendor/unlink")
    public ResponseEntity<String> unLinkProductWithVendor(@RequestBody VendorProductDeleteDto vendorProductDeleteDto){
        vendorProductService.deleteVendorProduct(vendorProductDeleteDto);
        return   new ResponseEntity<>("Product is un-linked with Vendor Successfully" , HttpStatus.CREATED);
    }

    @PutMapping("/{vendorProductId}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> updateVendorProduct(@RequestBody VendorProductUpdateDto vendorProductUpdateDto){
        vendorProductService.updateProductPerVendorPrice(vendorProductUpdateDto);
        return   new ResponseEntity<>("Updated Successfully" , HttpStatus.CREATED);
    }

    @GetMapping("category")
    public ResponseEntity<Page<VendorProductCategoryInfoDto>> getProductsPerVendorByCategoryId(
            @RequestParam long vendorId , @RequestParam long categoryId , @RequestParam int pageNumber , @RequestParam int pageSize){
        PageRequest pageable = PageRequest.of(pageNumber , pageSize);
        return ResponseEntity.ok(vendorProductService.getVendorProductsByCategory(vendorId , categoryId , pageable));
    }

    @GetMapping("brand")
    public ResponseEntity<Page<VendorProductBrandInfoDto>> getProductsPerVendorByBrandId(
            @RequestParam long vendorId , @RequestParam long brandId , @RequestParam int pageNumber , @RequestParam int pageSize){
        PageRequest pageable = PageRequest.of(pageNumber , pageSize);
        return ResponseEntity.ok(vendorProductService.getVendorProductsByBrand (vendorId , brandId , pageable));
    }
}
