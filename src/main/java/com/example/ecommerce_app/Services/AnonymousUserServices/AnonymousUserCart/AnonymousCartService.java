package com.example.ecommerce_app.Services.AnonymousUserServices.AnonymousUserCart;

import com.example.ecommerce_app.Dto.CartItem.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnonymousCartService {

    void addToCart(CartItemDto cartItemDto, String sessionId);

    List<CartItemResponseDto> getCartItemsBySessionId(String sessionId , Pageable pageable);

    void modifyCartItemQuantity(CartItemQuantityDto cartItemQuantityDto , String sessionId);

    void removeFromCart(RemoveFromCartDto removeFromCartDto , String sessionId);

    void removeAllFromCart( String sessionId);

}
