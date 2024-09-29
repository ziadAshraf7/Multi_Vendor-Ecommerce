package com.example.ecommerce_app.Controller;


import com.example.ecommerce_app.Dto.CartItem.CartItemCreationDto;
import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import com.example.ecommerce_app.Dto.CartItem.RemoveFromCartDto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Creation_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Detailed_Dto;
import com.example.ecommerce_app.Dto.ProductReview_Table.ProductReview_Update_Dto;
import com.example.ecommerce_app.Services.CartItem.CartItemService;
import com.example.ecommerce_app.Services.Cart_Management.Cart_Management_Service;
import com.example.ecommerce_app.Services.ProductReview.ProductReviewService;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/customer")
@AllArgsConstructor
public class CustomerController {

    private final ProductReviewService productReviewService;

    private final CartItemService cartItemService;

    private final Cart_Management_Service cartManagementService;


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

    @PostMapping("/cart")
    public ResponseEntity<String> addToCart(@RequestBody CartItemCreationDto cartItemCreationDto){

        cartManagementService.addToCart(cartItemCreationDto);
        return new ResponseEntity<>( "Added Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/cart")
    public ResponseEntity<List<CartItemDto>> getCartItems(@Param("id") long customerId){
        return ResponseEntity.ok(cartItemService.getCartItemsByCartId(customerId));
    }

    @DeleteMapping("/cart")
    public ResponseEntity<String> removeFromCart(RemoveFromCartDto removeFromCartDto){
        cartManagementService.removeFromCart(removeFromCartDto);
        return ResponseEntity.ok("Removed From Cart Successfully");
    }

}
