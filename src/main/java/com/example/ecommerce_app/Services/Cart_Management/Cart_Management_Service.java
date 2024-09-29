package com.example.ecommerce_app.Services.Cart_Management;

import com.example.ecommerce_app.Dto.CartItem.CartItemCreationDto;
import com.example.ecommerce_app.Dto.CartItem.CartItemQuantityDto;
import com.example.ecommerce_app.Dto.CartItem.RemoveFromCartDto;
import com.example.ecommerce_app.Entity.CartItem;

import java.util.List;

public interface Cart_Management_Service {

    void addToCart(CartItemCreationDto cartItemCreationDto);

    void removeFromCart(long productId);

    void addCollectionOfCartItems(List<CartItem> cartItems);

    void modifyCartItemQuantity(CartItemQuantityDto cartItemQuantityDto);

    void removeFromCart(RemoveFromCartDto removeFromCartDto);

    void removeAllFromCart(RemoveFromCartDto removeFromCartDto);

    void link_between_sessionCart_userCart(String sessionId , long userId);
}
