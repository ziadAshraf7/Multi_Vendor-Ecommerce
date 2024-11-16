package com.example.ecommerce_app.Services.CartItem;

import com.example.ecommerce_app.Dto.CartItem.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartItemService {


    List<CartItemResponseDto> getCartItemsByCartId(long customerId  ,Pageable pageable);

    boolean existsByProductId(long productId);

    void addToCart(AuthUserCartItemCreationDto authUserCartItemCreationDto);

    void modifyCartItemQuantity(CartItemQuantityDto cartItemQuantityDto);

    void removeFromCart(RemoveFromCartDto removeFromCartDto);

    void removeAllFromCart(long customerId);
}
