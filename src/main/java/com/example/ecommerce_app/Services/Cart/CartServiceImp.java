package com.example.ecommerce_app.Services.Cart;

import com.example.ecommerce_app.Dto.CartItem.CartItemCreationDto;
import com.example.ecommerce_app.Entity.Cart;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.NotFoundException;
import com.example.ecommerce_app.Repositery.Cart.CartRepository;
import com.example.ecommerce_app.Services.User.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Override
    public void createUserCart(User customer ) {

        try {

            Cart cart =  Cart.builder()
                    .totalPrice(0.0)
                    .customer(customer)
                    .build();

            cartRepository.save(cart);

        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to create cart for user id " + customer.getId());
        }


    }

    @Override
    public Cart getCartByCustomerId(long customerId) {
        try {
            return cartRepository.findByCustomerId(customerId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new NotFoundException("Unable to find cart for user id " + customerId  );
        }
    }

    @Override
    public void persistCartEntity(Cart cart) {
        try {
            cartRepository.save(cart);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to Update Cart Entity");
        }
    }
}
