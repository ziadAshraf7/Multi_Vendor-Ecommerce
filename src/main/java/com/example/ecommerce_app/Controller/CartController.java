package com.example.ecommerce_app.Controller;

import com.example.ecommerce_app.Dto.CartItem.*;
import com.example.ecommerce_app.Services.AnonymousUserServices.AnonymousUserCart.AnonymousCartService;
import com.example.ecommerce_app.Services.CartItem.CartItemService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private final AnonymousCartService anonymousCartService;

    private final CartItemService cartItemService;


    @PutMapping("/cartItem")
    public ResponseEntity<String> updateCartItemQuantity(@RequestBody CartItemQuantityDto cartItemQuantityDto , HttpSession httpSession){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            cartItemService.modifyCartItemQuantity(cartItemQuantityDto);
        }else {
            anonymousCartService.modifyCartItemQuantity(cartItemQuantityDto , httpSession.getId());
        }
        return new ResponseEntity<>( "Updated Successfully", HttpStatus.CREATED);
    }

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(@RequestBody @Valid CartItemDto cartItemDto , HttpSession httpSession){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() != "anonymousUser"){
            cartItemService.addToCart(cartItemDto);
        }else {
            anonymousCartService.addToCart(cartItemDto , httpSession.getId());
        }
        return new ResponseEntity<>( "Added Successfully", HttpStatus.CREATED);
    }

    @GetMapping("/cartItems")
    public ResponseEntity<List<CartItemResponseDto>> getCartItems(@RequestParam int pageNumber , @RequestParam int pageSize , HttpSession httpSession) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PageRequest pageable = PageRequest.of(pageNumber , pageSize);

        if(authentication.getPrincipal() != "anonymousUser"){
            System.out.println("ssss");
            return ResponseEntity.ok(cartItemService.getCartItems(pageable));
        }else {
            return ResponseEntity.ok(anonymousCartService.getCartItemsBySessionId(httpSession.getId() , pageable));
        }
    }

    @DeleteMapping("/cartItem")
    public ResponseEntity<String> removeFromCart(RemoveFromCartDto removeFromCartDto , HttpSession httpSession){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() != "anonymousUser"){
            cartItemService.removeFromCart(removeFromCartDto);
        }else {
            anonymousCartService.removeFromCart(removeFromCartDto , httpSession.getId());
        }

        return ResponseEntity.ok("Removed From Cart Successfully");
    }

    @DeleteMapping("/cart")
    public ResponseEntity<String> removeAllFromCart(@RequestParam Long customerId , HttpSession httpSession){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal() != "anonymousUser"){
            cartItemService.removeAllFromCart(customerId);
        }else {
            anonymousCartService.removeAllFromCart(httpSession.getId());
        }

        return ResponseEntity.ok("All Removed From Cart Successfully");
    }



}
