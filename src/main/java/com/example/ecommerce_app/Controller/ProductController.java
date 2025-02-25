package com.example.ecommerce_app.Controller;
import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Dto.Product_Table.*;
import com.example.ecommerce_app.Dto.Product_Table.Enums.ProductSortingByDtoEnum;
import com.example.ecommerce_app.Dto.Product_Table.Enums.SortingDirection;
import com.example.ecommerce_app.Dto.RecentlyViewedProducts.RecentlyViewedProductsDeleteDto;
import com.example.ecommerce_app.Dto.VendorProductTable.Vendor_Product_Image_Dto;
import com.example.ecommerce_app.Projections.RecentlyViewed.RecentlyViewedGeneralInfoView;
import com.example.ecommerce_app.Services.AdminRequestsApproval.AdminRequestsApprovalService;
import com.example.ecommerce_app.Services.ProductDisplay.ProductDisplayServiceImp;
import com.example.ecommerce_app.Services.Product.ProductServiceImp;
import com.example.ecommerce_app.Services.RecentlyViewedProducts.RecentlyViewedService;
import com.example.ecommerce_app.Services.VendorProductRequests.ProductRequestServiceForVendors;
import com.example.ecommerce_app.Services.Vendor_Product_Image.VendorProductImageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductServiceImp productService;

    private final ProductDisplayServiceImp productDisplayServiceImp;

    private final AdminRequestsApprovalService productApprovalService;

    private final VendorProductImageService vendorProductImageService;

    private final ProductRequestServiceForVendors productRequestServiceForVendors;

    private final RecentlyViewedService recentlyViewedService;

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<String> deleteProduct(@PathVariable long productId){
        productService.removeProduct(productId);
        return new ResponseEntity<>("Deleted Successfully" , HttpStatus.CREATED);
    }

    @PostMapping("/pending/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> acceptPendingProductRequest(@ModelAttribute @Valid ProductCreationDto productCreationDto ,
                                                              @ModelAttribute String notificationMessageId) throws IOException {
        productApprovalService.acceptProduct(productCreationDto , notificationMessageId);
        return new ResponseEntity<>("Created Successfully" , HttpStatus.CREATED);
    }

    @PostMapping("/request")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<String> SendAddingProductRequestToAdmin(
            @ModelAttribute
            ProductCreationDto productCreationDto) {

        productRequestServiceForVendors.sendProductCreationRequestToAdmin(productCreationDto);

        return new ResponseEntity<>(
                "Adding Product Request has been Successfully Sent" ,
                HttpStatus.OK);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateProduct(@ModelAttribute ProductUpdateDto productUpdateDto){
        productService.updateProduct(productUpdateDto);
        return new ResponseEntity<>("Created Successfully" , HttpStatus.OK);
    }


    @PostMapping("/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addImage(@Valid @ModelAttribute Vendor_Product_Image_Dto vendor_product_image_dto) throws IOException {
        vendorProductImageService.addProductImage(vendor_product_image_dto);
        return new ResponseEntity<>("Created Successfully" , HttpStatus.CREATED);
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductOverviewDto>> filterProducts(
            @RequestParam(required = false) Long brandId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) double[] priceRange,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) Boolean isDiscount,
            @RequestParam(required = false) ProductSortingByDtoEnum sortBy,
            @RequestParam(required = false) SortingDirection sortingDirection,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {

        ProductFilterDto productFilterDto = new ProductFilterDto();
        productFilterDto.setBrandId(brandId);
        productFilterDto.setCategoryId(categoryId);
        productFilterDto.setPriceRange(priceRange);
        productFilterDto.setRating(rating);
        productFilterDto.setIsDiscount(isDiscount);
        productFilterDto.setSortBy(sortBy);
        productFilterDto.setSortingDirection(sortingDirection);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(productService.filterProducts(productFilterDto, pageRequest));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailedDto> getProduct(@PathVariable long productId) {
        return ResponseEntity.ok(productDisplayServiceImp.getProduct(productId));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductOverviewDto>> getProductsByCategoryId(
            @PathVariable long categoryId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId, pageRequest));
    }

    @GetMapping("/brand/{brandId}")
    public ResponseEntity<Page<ProductOverviewDto>> getProductsByBrandId(
            @PathVariable long brandId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(productService.getProductsPerBrand(brandId, pageRequest));
    }

    @GetMapping("/category/{categoryId}/bestsellers")
    public ResponseEntity<Page<ProductOverviewDto>> getBestSellerProductsPerCategory(
            @PathVariable long categoryId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(productService.getBestSellerProductsPerCategory(categoryId, pageRequest));
    }

    @GetMapping("/brand/{brandId}/bestsellers")
    public ResponseEntity<Page<ProductOverviewDto>> getBestSellerProductsPerBrand(
            @PathVariable long brandId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(productService.getBestSellerProductsPerBrand(brandId, pageRequest));
    }

    @GetMapping("/category/{categoryId}/most-viewed")
    public ResponseEntity<Page<ProductOverviewDto>> getMostViewedProductsPerCategory(
            @PathVariable long categoryId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(productService.getMostViewedProductsPerCategory(categoryId, pageRequest));
    }

    @GetMapping("/brand/{brandId}/most-viewed")
    public ResponseEntity<Page<ProductOverviewDto>> getMostViewedProductsPerBrand(
            @PathVariable long brandId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return ResponseEntity.ok(productService.getMostViewedProductsPerBrand(brandId, pageRequest));
    }

    @GetMapping("/ViewedProduct")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<RecentlyViewedGeneralInfoView>> getAllRecentlyViewedProducts(){
        long authenticatedUserId = ((AuthenticatedUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        return ResponseEntity.ok(recentlyViewedService.getAllRecentlyViewedByUser(authenticatedUserId));
    }

    @DeleteMapping("/ViewedProduct")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> deleteRecentlyViewedProduct(@RequestParam("productId") long productId ){
        long authenticatedUserId = ((AuthenticatedUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        RecentlyViewedProductsDeleteDto recentlyViewedProductsDeleteDto = new RecentlyViewedProductsDeleteDto(productId , authenticatedUserId);
        recentlyViewedService.deleteRecentlyViewedProduct(recentlyViewedProductsDeleteDto);
        return new ResponseEntity<>("deleted successfully" , HttpStatus.CREATED);
    }
}
