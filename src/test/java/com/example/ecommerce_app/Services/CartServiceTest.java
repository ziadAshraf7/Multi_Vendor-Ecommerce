package com.example.ecommerce_app.Services;

import com.example.ecommerce_app.Entity.Cart;
import com.example.ecommerce_app.Entity.User;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.DatabasePersistenceException;
import com.example.ecommerce_app.Repositery.Cart.CartRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Services.Cart.CartService;
import com.example.ecommerce_app.Services.Cart.CartServiceImp;
import com.example.ecommerce_app.Utills.Interfaces.UserRoles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImp cartService;

    private User customer;

    private Cart cart;

    @BeforeEach
    public void init(){
        customer = User.builder()
                .id(1L)
                .email("ziad@gmail.com")
                .address("benha")
                .password("123456")
                .userName("ziad")
                .userRole(UserRoles.ROLE_CUSTOMER)
                .build();

        cart = Cart
                .builder()
                .customerId(1L)
                .customer(customer)
                .totalPrice(0)
                .build();
    }

    @Test
    void CartServiceCreateUserCartReturnsVoid(){

        assertDoesNotThrow(() -> {
            cartService.createUserCart(customer);
        });

        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void CartServiceGetCartByCustomerIdReturnCartEntity(){

        when(cartRepository.findByCustomerId(1L)).thenReturn(cart);

          Cart cart = cartService.getCartByCustomerId(1L);

          assertThat(cart).isNotEqualTo(null);
          assertThat(cart.getCustomerId()).isEqualTo(1L);
    }

    @Test
    void CartServicePersistCartEntityReturnCartEntity(){
        Cart cartEntity = Cart.builder().totalPrice(0.0).build();
        assertDoesNotThrow(() -> cartService.persistCartEntity(cart));
    }

    @Test
    void CartServiceCreateUserThrowsCustomBadRequestException(){
        CustomBadRequestException exception =  assertThrows(CustomBadRequestException.class , () -> cartService.createUserCart(null));
        assertEquals(exception.getMessage() , "Customer Entity cannot be null");
    }

    @Test
    void CartServiceCreateUserThrowsDatabasePersistenceException(){

        when(cartRepository.save(any(Cart.class)))
                .thenThrow(new DatabasePersistenceException("Error saving cart"));

        DatabasePersistenceException exception = assertThrows(DatabasePersistenceException.class, () -> {
            cartService.createUserCart(customer);
        });

        assertEquals("Unable to create cart for user id " + customer.getId(), exception.getMessage());
    }

    @Test
    void CartServiceGetCartByCustomerIdThrowsCustomNotFoundException() {

        long customerId = 5L;

        CustomNotFoundException exception = assertThrows(CustomNotFoundException.class , () -> cartService.getCartByCustomerId(customerId));

        assertEquals(exception.getMessage() , "customer user is not found for user id " + customerId);
    }

    @Test
    void CartServicePersistCartEntityThrowsDatabasePersistenceException(){

        when(cartRepository.save(any(Cart.class)))
                .thenThrow(new DatabasePersistenceException("Error saving cart"));

        DatabasePersistenceException exception = assertThrows(DatabasePersistenceException.class, () -> {
            cartService.persistCartEntity(cart);
        });

        assertEquals("Unable to Update Cart Entity", exception.getMessage());
    }
    }
