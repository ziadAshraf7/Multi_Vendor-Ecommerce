package com.example.ecommerce_app.Services.Cart_Management;


import com.example.ecommerce_app.Dto.CartItem.CartItemCreationDto;
import com.example.ecommerce_app.Dto.CartItem.CartItemDto;
import com.example.ecommerce_app.Dto.CartItem.CartItemQuantityDto;
import com.example.ecommerce_app.Dto.CartItem.RemoveFromCartDto;
import com.example.ecommerce_app.Entity.Cart;
import com.example.ecommerce_app.Entity.CartItem;
import com.example.ecommerce_app.Entity.Embedded_Ids.CartItem_EmbeddedId;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.Vendor_Product;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomBadRequestException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Mapper.CartItemMapper;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
import com.example.ecommerce_app.Services.Cart.CartService;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Redis.Session.Session_Management.SessionService;
import com.example.ecommerce_app.Services.Vendor_Product.Vendor_Product_Service;
import com.example.ecommerce_app.Redis.Session.AnonymousUser.AnonymousCartService;
import com.example.ecommerce_app.Utills.UtilsClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class Cart_Management_Service_Imp implements Cart_Management_Service{

    private final CartItemRepository cartItemRepository;

    private final CartService cartService;

    private final ProductService productService;

    private final Vendor_Product_Service vendor_product_service;

    private final CartItemMapper cartItemMapper;

    private final AnonymousCartService anonymousCartService;

    private SessionService sessionService;

    @Override
    @Transactional
    public void modifyCartItemQuantity(CartItemQuantityDto cartItemQuantityDto) {

        try {
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(
                    cartItemQuantityDto.getProductId() ,
                    cartItemQuantityDto.getCustomerId()
            );

            Cart cart = cartService.getCartByCustomerId(cartItemQuantityDto.getCustomerId());

            int oldProductQuantity = cartItem.getQuantity();
            int newQuantity = cartItemQuantityDto.getQuantity();
            double productPrice = cartItem.getPrice();
            cartItem.setQuantity(newQuantity);

            cart.setTotalPrice(cart.getTotalPrice() - (oldProductQuantity * cartItem.getPrice()) + newQuantity * productPrice );

            cartItemRepository.save(cartItem);

        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    @Override
    @Transactional
    public void addToCart(CartItemCreationDto cartItemCreationDto) {

        Cart cart = cartService.getCartByCustomerId(
                cartItemCreationDto.getCustomerId()
        );

        Product product = productService.getProductEntityById(
                cartItemCreationDto.getProductId()
        );

        Vendor_Product vendor_product = vendor_product_service.getByVendorIdAndProductId(
                cartItemCreationDto.getVendorId(),
                cartItemCreationDto.getProductId()
        );

        try {

            int productStock = vendor_product.getStock();

            if(productStock == 0) throw new CustomBadRequestException("product is ran out of stock");

            if(cartItemCreationDto.getQuantity() > productStock) throw new CustomBadRequestException("quantity selected is greater than product stock");

            cart.setTotalPrice(cart.getTotalPrice() + cartItemCreationDto.getQuantity() * vendor_product.getPrice());

            CartItem cartItem = CartItem.builder()
                    .id(new CartItem_EmbeddedId())
                    .cart(cart)
                    .price(UtilsClass.calcProductFinalPrice(vendor_product.getPrice() , vendor_product.getDiscount()))
                    .stock(vendor_product.getStock())
                    .discount(vendor_product.getDiscount())
                    .product(product)
                    .quantity(cartItemCreationDto.getQuantity())
                    .build();

            cartItemRepository.save(cartItem);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to add product to the cart");
        }

    }

    @Override
    @Transactional
    public void removeFromCart(RemoveFromCartDto removeFromCartDto) {
        try {
            Cart cart = cartService.getCartByCustomerId(removeFromCartDto.getCustomerId());
            CartItem cartItem = cartItemRepository.findByProductIdAndCartId(
                    removeFromCartDto.getProductId(),
                    removeFromCartDto.getCustomerId()
            );

            double newTotalPrice = cart.getTotalPrice() - (cartItem.getPrice() * cartItem.getQuantity());
            cart.setTotalPrice(newTotalPrice);
            cartService.persistCartEntity(cart);
            cartItemRepository.delete(cartItem);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to delete product from cart");
        }
    }

    @Override
    @Transactional
    public void removeFromCart(long productId) {
        try {
            cartItemRepository.deleteByProductId(productId);
        }catch (RuntimeException e){
            throw new CustomRuntimeException("unable to remove product from cart");
        }
    }

    @Override
    @Transactional
    public void removeAllFromCart(RemoveFromCartDto removeFromCartDto) {
        try {
            Cart cart = cartService.getCartByCustomerId(removeFromCartDto.getCustomerId());
            cart.setTotalPrice(0);
            cartService.persistCartEntity(cart);
            cartItemRepository.deleteALLbYCustomerId(removeFromCartDto.getCustomerId());
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Unable to remove all Products from Cart");
        }
    }

    @Override
    @Transactional
    public void link_between_sessionCart_userCart(String sessionId , long userId) {

        List<CartItemDto> sessionCartItems = anonymousCartService.getCartItemsBySessionId(sessionId);

        List<CartItem> userCartItems = cartItemRepository.findByCartId(userId);

        sessionCartItems.sort(Comparator.comparing(CartItemDto::getName));
        userCartItems.sort(Comparator.comparing(cartItem -> cartItem.getProduct().getName()));

        for(int i = 0 ; i < Math.max(sessionCartItems.size() - 1 , userCartItems.size()) ; i++){
            CartItemDto sessionCartItem = null ;
            CartItem userCartItem = null;

            if(i < sessionCartItems.size()){
                sessionCartItem = sessionCartItems.get(i);
            }

            if(i < userCartItems.size()){
                userCartItem = userCartItems.get(i);
            }

            if((sessionCartItem != null && userCartItem != null) &&
                    Objects.equals(sessionCartItem.getName(), userCartItem.getProduct().getName())){
                return;
            }else if(sessionCartItem != null){
                addToCart(CartItemCreationDto.builder()
                        .productId(sessionCartItem.getProductId())
                        .customerId(userId)
                        .vendorId(sessionCartItem.getVendorId())
                        .quantity(sessionCartItem.getQuantity())
                        .build());
            }

        }
        sessionService.deleteSession(sessionId);
    }

    @Override
    @Transactional
    public void addCollectionOfCartItems(List<CartItem> cartItems) {
        try {
            cartItemRepository.saveAll(cartItems);
        }catch (RuntimeException e){
            throw new CustomRuntimeException("unable to add cart items");
        }
    }


}
