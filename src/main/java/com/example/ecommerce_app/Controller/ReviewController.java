package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReviewCreationDto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReviewTable.ProductReview_Update_Dto;
import com.example.ecommerce_app.Services.ProductReview.ProductReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/review")
@AllArgsConstructor
public class ReviewController {

    private final ProductReviewService productReviewService;

    @PostMapping
    public ResponseEntity<String> addReview(@ModelAttribute ProductReviewCreationDto productReview_creation_dto){
        productReviewService.addReview(productReview_creation_dto);
        return new ResponseEntity<>( "Created Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductReview_Detailed_Dto>> getReviewsPerUser(@PathVariable("userId") long userId){
        return ResponseEntity.ok(productReviewService.getAllReviewsPerUser(userId));
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ProductReview_Detailed_Dto>> getReviewsPerProduct(@PathVariable("productId") long productId){
        return ResponseEntity.ok(productReviewService.getAllReviewsPerProduct(productId));
    }

    @DeleteMapping("product/{productId}")
    public ResponseEntity<String> deleteReview(@PathVariable("productId") long productId ){
        productReviewService.removeReview(productId);
        return new ResponseEntity<>( "Deleted Successfully", HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<String> updateReview(@ModelAttribute ProductReview_Update_Dto productReview_update_dto) throws IOException {
        productReviewService.updateReview(productReview_update_dto);
        return new ResponseEntity<>( "Updated Successfully", HttpStatus.CREATED);
    }


}
