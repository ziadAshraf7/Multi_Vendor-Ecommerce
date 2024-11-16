package com.example.ecommerce_app.Services.Cart;

import com.example.ecommerce_app.Entity.Cart;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Cart.CartRepository;
import com.example.ecommerce_app.Services.User.UserService;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
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


            if(customer == null ) throw new CustomBadRequestException("Customer Entity cannot be null");

            if(customer.getUserRole() != UserRoles.ROLE_CUSTOMER) throw new CustomBadRequestException("User is not a customer user");

            Cart existibgCart = cartRepository.findByCustomerId(customer.getId());

            if(existibgCart != null) throw new CustomBadRequestException("User cannot have multiple carts");

            Cart cart =  Cart.builder()
                    .customer(customer)
                    .build();
        try {
            cartRepository.save(cart);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to create cart for user id " + customer.getId());
        }


    }

    @Transactional(readOnly = true)
    @Override
    public Cart getCartByCustomerId(long customerId) {
          return cartRepository.findById(customerId).orElseThrow(
                  () -> new CustomNotFoundException("cart is not found for user id " + customerId )
          );
    }

    @Transactional
    @Override
    public Cart persistCartEntity(Cart cart) {
        try {
         return cartRepository.save(cart);
        }catch (DatabasePersistenceException e){
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to Update Cart Entity");
        }
    }
}
