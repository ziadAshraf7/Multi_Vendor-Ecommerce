package com.example.ecommerce_app.Redis.Session.AnonymousUser;

import com.example.ecommerce_app.Dto.CartItem.*;
import com.example.ecommerce_app.Dto.CartItem.Session.SessionUserCartItemQuantityDto;

import java.util.List;

public interface AnonymousCartService {

    void addToCart(CartItemDto cartItemDto, String sessionId);

    List<CartItemDto> getCartItemsBySessionId(String sessionId);

    void modifyCartItemQuantity(SessionUserCartItemQuantityDto sessionUserCartItemQuantityDto , String sessionId);

    void removeFromCart(RemoveFromCartDto removeFromCartDto , String sessionId);

    void removeAllFromCart(RemoveFromCartDto removeFromCartDto , String sessionId);

}
