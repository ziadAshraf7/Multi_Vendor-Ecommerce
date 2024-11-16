package com.example.ecommerce_app.Redis.Session.AnonymousUser;

import com.example.ecommerce_app.Dto.CartItem.*;

import java.util.List;

public interface AnonymousCartService {

    void addToCart(CartItemDto cartItemDto, String sessionId);

    List<CartItemResponseDto> getCartItemsBySessionId(String sessionId);

    void modifyCartItemQuantity(CartItemQuantityDto cartItemQuantityDto , String sessionId);

    void removeFromCart(RemoveFromCartDto removeFromCartDto , String sessionId);

    void removeAllFromCart(RemoveFromCartDto removeFromCartDto , String sessionId);

}
