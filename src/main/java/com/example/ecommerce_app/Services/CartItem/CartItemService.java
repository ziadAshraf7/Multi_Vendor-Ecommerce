package com.example.ecommerce_app.Services.CartItem;

import com.example.ecommerce_app.Dto.CartItem.CartItemCreationDto;
import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import com.example.ecommerce_app.Dto.CartItem.CartItemQuantityDto;
import com.example.ecommerce_app.Dto.CartItem.RemoveFromCartDto;
import com.example.ecommerce_app.Entity.CartItem;

import java.util.List;

public interface CartItemService {


    List<CartItemDto> getCartItemsByCartId(long userId);

    boolean existsByProductId(long productId);

}
