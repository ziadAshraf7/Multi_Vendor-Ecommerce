package com.example.ecommerce_app.Services.CartItem;
import com.example.ecommerce_app.Dto.CartItem.*;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Entity.Embedded_Ids.CartItem_EmbeddedId;
import com.example.ecommerce_app.Exceptions.Exceptions.*;
import com.example.ecommerce_app.Mapper.CartItemMapper;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Cart.CartService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@Slf4j
@Service
public class CartItemServiceImp implements CartItemService{

    private final CartItemRepository cartItemRepository;

    private final CartItemMapper cartItemMapper;

    private final CartService cartService;

    private final VendorProductRepository vendorProductRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CartItemResponseDto> getCartItemsByCartId(long customerId , Pageable pageable ) {
        try {
            Page<CartItem> cartItems = cartItemRepository.findByCartId(customerId , pageable);
            List<CartItemResponseDto> cartItemDtos = new ArrayList<>(cartItems.getNumberOfElements());
            for(CartItem cartItem : cartItems) cartItemDtos.add(cartItemMapper.fromEntityToResponseDto(cartItem));
            return cartItemDtos;
        }catch (CustomNotFoundException e){
            log.error(e.getMessage());
            throw new CustomNotFoundException("unable to retrieve cart items");
        }
    }


    @Transactional(readOnly = true)
    @Override
    public boolean existsByProductId(long productId) {
        try {
            return cartItemRepository.existsByProductId(productId);
        }catch (CustomNotFoundException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("encountered error while checking the existing of a cart item");
        }
    }

    @Override
    @Transactional
    public void modifyCartItemQuantity(CartItemQuantityDto cartItemQuantityDto) {

        VendorProduct vendorProduct = vendorProductRepository.findById(
                cartItemQuantityDto.getVendorProductId()
        ).orElseThrow(() -> new CustomNotFoundException("vendor product is not found"));

        CartItem cartItem = cartItemRepository.findByProductIdAndCartIdAndVendorProductId(
                cartItemQuantityDto.getProductId(),
                cartItemQuantityDto.getCustomerId(),
                cartItemQuantityDto.getVendorProductId()
        );

        if (cartItem == null) throw new CustomNotFoundException("Unable to find cart item");

        int newQuantity = cartItemQuantityDto.getQuantity();
        if (vendorProduct.getStock() >= newQuantity) {
            cartItem.setQuantity(newQuantity);
        } else {
            throw new CustomBadRequestException("product is ran out of stock");
        }

        try {
            cartItemRepository.save(cartItem);
        } catch (DatabasePersistenceException e) {
            log.error(e.getMessage());
            throw new DatabasePersistenceException("unable to update cart item ");
        }
    }


    @Override
    @Transactional
    public void addToCart(AuthUserCartItemCreationDto authUserCartItemCreationDto) {

        Cart cart = cartService.getCartByCustomerId(authUserCartItemCreationDto.getCustomerId());

        VendorProduct vendorProduct = vendorProductRepository.findById(authUserCartItemCreationDto.getVendorProductId())
                .orElseThrow(() -> new CustomNotFoundException("vendorProduct is not found"));


        if (vendorProduct == null) throw new CustomNotFoundException("Cannot find vendor product");
        Product product = vendorProduct.getProduct();

        int productStock = vendorProduct.getStock();

        if (productStock == 0) throw new CustomBadRequestException("product is ran out of stock");

        if (authUserCartItemCreationDto.getQuantity() > productStock)
            throw new CustomConflictException("quantity selected is greater than product stock");

        CartItem cartItem = CartItem.builder()
                .id(new CartItem_EmbeddedId(
                        authUserCartItemCreationDto.getCustomerId(),
                        authUserCartItemCreationDto.getProductId(),
                        authUserCartItemCreationDto.getVendorProductId()
                ))
                .vendorProduct(vendorProduct)
                .cart(cart)
                .product(product)
                .quantity(authUserCartItemCreationDto.getQuantity())
                .build();
        try {
            cartItemRepository.save(cartItem);
        } catch (DatabasePersistenceException e) {
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Cart item is already existed in cart");
        }

    }

    @Override
    @Transactional
    public void removeFromCart(RemoveFromCartDto removeFromCartDto) {
        try {
            CartItem cartItem = cartItemRepository.findByProductIdAndCartIdAndVendorProductId(
                    removeFromCartDto.getProductId(),
                    removeFromCartDto.getCustomerId(),
                    removeFromCartDto.getVendorProductId()
            );
            if (cartItem == null) throw new CustomNotFoundException("unable to find cart item");
            cartItemRepository.delete(cartItem);
        } catch (DatabasePersistenceException e) {
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to delete product from cart");
        }
    }


    @Override
    @Transactional
    public void removeAllFromCart(long customerId) {
        try {
            Cart cart = cartService.getCartByCustomerId(customerId);
            if (cart == null) throw new CustomNotFoundException("cart is not found");
            cartItemRepository.deleteAllBYCustomerId(customerId);
        } catch (DatabasePersistenceException e) {
            log.error(e.getMessage());
            throw new DatabasePersistenceException("Unable to remove all Products from Cart");
        }
    }

}
