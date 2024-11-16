package com.example.ecommerce_app.Services.Cart;

import com.example.ecommerce_app.Entity.Cart;
import com.example.ecommerce_app.Entity.User;


public interface CartService {

        void createUserCart(User customer);

        Cart getCartByCustomerId(long customerId);

        Cart persistCartEntity(Cart cart);
}
