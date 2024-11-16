package com.example.ecommerce_app.Redis.Session.AnonymousUser;

import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AnonymousUserCartData implements Serializable {


    private List<CartItemDto> cartItems;


    public void addToCartItems(CartItemDto cartItem ){
        cartItems.add(cartItem);
    }

    public CartItemDto findCartItem(long productId , long vendorProductId){
        for (CartItemDto cartItem : cartItems){
            if (cartItem.getProductId() == productId && cartItem.getVendorProductId() == vendorProductId) return cartItem;
        }
        return null;
    }

    public AnonymousUserCartData updateCartItemData(long productId , long vendorProductId , int quantity ){
        for (CartItemDto cartItem : cartItems){
            if (cartItem.getProductId() == productId && cartItem.getVendorProductId() == vendorProductId) {
                cartItem.setQuantity(quantity);
            };
        }
        return this;
    }

    public AnonymousUserCartData removeFromCartItems(long productId , long vendorProductId){
        cartItems.removeIf((cartItem) -> cartItem.getProductId() == productId && cartItem.getVendorProductId() == vendorProductId);
        return this;
    }

    public CartItemDto getCartItem(long productId , long vendorProductId){
        for (CartItemDto cartItem : cartItems) {
            if(cartItem.getProductId() == productId && cartItem.getVendorProductId() == vendorProductId) return cartItem;
        }
        return null;
    }

}
