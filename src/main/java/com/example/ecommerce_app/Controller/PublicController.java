package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Creation_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Update_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Detailed_Dto;
import com.example.ecommerce_app.Dto.Product_Table.Product_Overview_Dto;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Services.ProductReview.ProductReviewService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/public")
@AllArgsConstructor
public class PublicController {

    private final ProductService productService;

    @GetMapping("/product/{productId}")
    public ResponseEntity<Product_Detailed_Dto> getProduct(@PathVariable long productId){
        return ResponseEntity.ok(productService.getProductById(productId));
    }

    @GetMapping("/products/category")
    public ResponseEntity<Page<Product_Overview_Dto>> getProductsByCategoryId(@Param("categoryId") long categoryId){
        return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId));
    }

    @GetMapping("/products/brand")
    public ResponseEntity<Page<Product_Overview_Dto>> getProductsByBrandId(@Param("brandId") long brandId){
        return ResponseEntity.ok(productService.getProductsPerBrand(brandId));
    }

    @GetMapping("/products/bestseller/category")
    public ResponseEntity<Page<Product_Overview_Dto>> getBestSellerProductsPerCategory(
            @Param("categoryId") long categoryId){
        return ResponseEntity.ok(productService.getBestSellerProductsPerCategory(categoryId));
    }

    @GetMapping("/products/bestseller/brand")
    public ResponseEntity<Page<Product_Overview_Dto>> getBestSellerProductsPerBrand(
            @Param("brandId") long brandId){
        return ResponseEntity.ok(productService.getProductsPerBrand(brandId));
    }

    @GetMapping("/products/mostViews/category")
    public ResponseEntity<Page<Product_Overview_Dto>> getMostViewsProductsPerCategory(
            @Param("categoryId") long categoryId){
        return ResponseEntity.ok(productService.getBestSellerProductsPerCategory(categoryId));
    }

    @GetMapping("/products/mostViews/brand")
    public ResponseEntity<Page<Product_Overview_Dto>> getMostViewsProductsPerBrand(
            @Param("brandId") long brandId){
        return ResponseEntity.ok(productService.getProductsPerBrand(brandId));
    }

}
