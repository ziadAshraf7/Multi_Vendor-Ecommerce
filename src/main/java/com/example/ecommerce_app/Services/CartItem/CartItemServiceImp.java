package com.example.ecommerce_app.Services.CartItem;
import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Mapper.CartItemMapper;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Slf4j
@Service
public class CartItemServiceImp implements CartItemService{

    private final CartItemRepository cartItemRepository;

    private final CartItemMapper cartItemMapper;


    @Override
    public List<CartItemDto> getCartItemsByCartId(long customerId) {
        try {
            List<CartItem> cartItems = cartItemRepository.findByCartId(customerId);
            List<CartItemDto> cartItemDtos = new ArrayList<>(cartItems.size());
            for(CartItem cartItem : cartItems) cartItemDtos.add(cartItemMapper.fromEntityToResponseDto(cartItem));
            return cartItemDtos;
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomNotFoundException("unable to retrieve cart items");
        }
    }


    @Override
    public boolean existsByProductId(long productId) {
        try {
            return cartItemRepository.existsByProductId(productId);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("encountered error while checking the existing of a cart item");
        }
    }







}
