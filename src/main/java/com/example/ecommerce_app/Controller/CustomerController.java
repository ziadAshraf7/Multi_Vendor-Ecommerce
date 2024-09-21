package com.example.ecommerce_app.Controller;


import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Creation_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Update_Dto;
import com.example.ecommerce_app.Services.ProductReview.ProductReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    private final ProductReviewService productReviewService;

    @PostMapping("/review")
    public ResponseEntity<String> addReview(@ModelAttribute ProductReview_Creation_Dto productReview_creation_dto){
            productReviewService.addReview(productReview_creation_dto);
            return new ResponseEntity<>( "Created Successfully", HttpStatus.CREATED);
    }


    @GetMapping("/review/{id}")
    public ResponseEntity<List<ProductReview_Detailed_Dto>> getReviews(@PathVariable("id") long id){
        return ResponseEntity.ok(productReviewService.getAllReviewsPerProduct(id));
    }

    @DeleteMapping("/review")
    public ResponseEntity<String> deleteReview(@Param("productId") long productId , @Param("userId") long userId){
            productReviewService.removeReview(userId , productId);
            return new ResponseEntity<>( "Deleted Successfully", HttpStatus.CREATED);
       }


    @PutMapping("review")
    public ResponseEntity<String> updateReview(@ModelAttribute ProductReview_Update_Dto productReview_update_dto) throws IOException {
            productReviewService.updateReview(productReview_update_dto);
            return new ResponseEntity<>( "Updated Successfully", HttpStatus.CREATED);
    }


}
