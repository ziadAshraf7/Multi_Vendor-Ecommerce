package com.example.ecommerce_app.Redis.Session.SessionData;

import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Projections.RecentlyViewed.RecentlyViewedGeneralInfoView;
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
public class AnonymousUserSessionData implements Serializable {

    private List<CartItemDto> cartItems;

    public void addToCartItems(CartItemDto cartItem ){
        cartItems.add(cartItem);
    }

    public AnonymousUserSessionData updateCartItemData(long productId , long vendorProductId , int quantity ){
        boolean isUpdated = false;
        for (CartItemDto cartItem : cartItems){
            if (cartItem.getProductId() == productId && cartItem.getVendorProductId() == vendorProductId) {
                cartItem.setQuantity(quantity);
                isUpdated = true;
            };
        }
        if(!isUpdated) throw new CustomNotFoundException("Product is not found in the cart");
        return this;
    }

    public AnonymousUserSessionData removeFromCartItems(long productId , long vendorProductId){
        boolean isRemoved = cartItems.removeIf((cartItem) -> cartItem.getProductId() == productId && cartItem.getVendorProductId() == vendorProductId);
        System.out.println(isRemoved);
        System.out.println("vendorProductId");
        if(!isRemoved) throw new CustomNotFoundException("Product is not found in the cart");
        return this;
    }

    public CartItemDto getCartItem(long productId , long vendorProductId){
        for (CartItemDto cartItem : cartItems) {
            if(cartItem.getProductId() == productId && cartItem.getVendorProductId() == vendorProductId) return cartItem;
        }
        return null;
    }

}
