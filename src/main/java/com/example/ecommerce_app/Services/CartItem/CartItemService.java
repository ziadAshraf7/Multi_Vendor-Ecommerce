package com.example.ecommerce_app.Services.CartItem;

import com.example.ecommerce_app.Dto.CartItem.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartItemService {


    List<CartItemResponseDto> getCartItems(Pageable pageable);

    boolean existsByProductId(long productId);

    void addToCart(CartItemDto cartItemDto);

    void modifyCartItemQuantity(CartItemQuantityDto cartItemQuantityDto);

    void removeFromCart(RemoveFromCartDto removeFromCartDto);

    void removeAllFromCart(long userId);
}
