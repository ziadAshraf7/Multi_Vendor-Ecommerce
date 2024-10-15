package com.example.ecommerce_app.Services.Cart;

import com.example.ecommerce_app.Entity.Cart;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Cart.CartRepository;
import com.example.ecommerce_app.Services.User.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class CartServiceImp implements CartService{

    private final CartRepository cartRepository;

    private final UserService userService;

    @Transactional
    @Override
    public void createUserCart(User customer ) {

        try {

            if(customer == null ) throw new CustomBadRequestException("Customer Entity cannot be null");

            Cart cart =  Cart.builder()
                    .totalPrice(0.0)
                    .customer(customer)
                    .build();

            cartRepository.save(cart);

        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to create cart for user id " + customer.getId());
        }


    }

    @Override
    public Cart getCartByCustomerId(long customerId) {
          Cart cart = cartRepository.findByCustomerId(customerId);

          if(cart == null) throw new CustomNotFoundException("customer user is not found for user id " + customerId);

          return cart;
    }

    @Transactional
    @Override
    public Cart persistCartEntity(Cart cart) {
        try {
         return   cartRepository.save(cart);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to Update Cart Entity");
        }
    }
}
