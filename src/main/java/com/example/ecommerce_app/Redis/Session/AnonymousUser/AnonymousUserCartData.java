package com.example.ecommerce_app.Redis.Session.AnonymousUser;

import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AnonymousUserCartData {

    private double totalCartPrice;

    private List<CartItemDto> cartItems;


    public void addToCartItems(CartItemDto cartItem){
        this.totalCartPrice = cartItem.getQuantity() * cartItem.getPrice();
        cartItems.add(cartItem);
    }

    public CartItemDto findCartItem(long productId){
        for (CartItemDto cartItem : cartItems){
            if (cartItem.getProductId() == productId) return cartItem;
        }
        return null;
    }

    public AnonymousUserCartData updateCartItemData(long productId , int quantity , double price){
        for (CartItemDto cartItem : cartItems){
            if (cartItem.getProductId() == productId) {
                cartItem.setQuantity(quantity);
                double oldTotalPricePerProduct = cartItem.getQuantity() * price;
                double newTotalPricePerProduct = quantity * price;
                totalCartPrice = totalCartPrice + (newTotalPricePerProduct - oldTotalPricePerProduct);
            };
        }
        return this;
    }

    public AnonymousUserCartData removeFromCartItems(long productId){

        cartItems.removeIf((cartItem) -> {
            if(cartItem.getProductId() == productId){
                totalCartPrice = totalCartPrice - (cartItem.getQuantity() * cartItem.getPrice());
                return true;
            }else return false;
        });
           return this;
    }

    public CartItemDto getCartItem(long productId){
        for (CartItemDto cartItem : cartItems) {

            if(cartItem.getProductId() == productId) return cartItem;

        }
        return null;
    }

}
