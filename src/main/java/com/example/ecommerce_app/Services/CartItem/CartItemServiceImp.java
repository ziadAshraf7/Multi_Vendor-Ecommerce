package com.example.ecommerce_app.Services.CartItem;
import com.example.ecommerce_app.Dto.AutheticatedUser.AuthenticatedUserDto;
import com.example.ecommerce_app.Dto.CartItem.*;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Entity.Embedded_Ids.CartItemEmbeddedId;
import com.example.ecommerce_app.Exceptions.Exceptions.*;
import com.example.ecommerce_app.Mapper.CartItemMapper;
import com.example.ecommerce_app.Repositery.Cart.CartRepository;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
import com.example.ecommerce_app.Repositery.User.UserRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Cart.CartService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final CartRepository cartRepository;

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CartItemResponseDto> getCartItems(Pageable pageable ) {
        long authenticatedUserId = ((AuthenticatedUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        User user = userRepository.findById(authenticatedUserId).orElseThrow(() -> new CustomNotFoundException("user is not found"));
            Page<CartItem> cartItems = cartItemRepository.findByCartId(user.getCart().get(0).getId() , pageable);
            List<CartItemResponseDto> cartItemDtos = new ArrayList<>(cartItems.getNumberOfElements());
            for(CartItem cartItem : cartItems) cartItemDtos.add(cartItemMapper.fromEntityToResponseDto(cartItem));
            return cartItemDtos;
    }


    @Transactional(readOnly = true)
    @Override
    public boolean existsByProductId(long productId) {
            return cartItemRepository.existsByProductId(productId);
    }

    @Override
    @Transactional
    public void modifyCartItemQuantity(CartItemQuantityDto cartItemQuantityDto) {

        long authenticatedUserId = ((AuthenticatedUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        VendorProduct vendorProduct = vendorProductRepository.findByVendorProductId(
                cartItemQuantityDto.getVendorProductId()
        );

        if(vendorProduct == null) throw new CustomNotFoundException("vendor product is not found");

        CartItem cartItem = cartItemRepository.findByProductIdAndCartIdAndVendorProductId(
                vendorProduct.getProduct().getId(),
                cartItemQuantityDto.getCartId(),
                vendorProduct.getId()
        );
        if (cartItem == null) throw new CustomNotFoundException("Unable to find cart item");

        if(cartItem.getCart().getCustomer().getId() != authenticatedUserId) throw new CustomBadRequestException("user is not authorized");

        int newQuantity = cartItemQuantityDto.getQuantity();
        if (vendorProduct.getStock() >= newQuantity) {
            cartItem.setQuantity(newQuantity);
        } else {
            throw new CustomBadRequestException("product is ran out of stock");
        }
            cartItemRepository.save(cartItem);
    }


    @Override
    @Transactional
    public void addToCart(CartItemDto cartItemDto) {

        long authenticatedUserId = ((AuthenticatedUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

        Cart cart = cartService.getCartByCustomerId(authenticatedUserId);

        if(cart == null) throw new CustomNotFoundException("cart is not found");

        VendorProduct vendorProduct = vendorProductRepository.findByVendorProductId(cartItemDto.getVendorProductId());
        if (vendorProduct == null) throw new CustomNotFoundException("Cannot find vendor product");

        Product product = vendorProduct.getProduct();

        int productStock = vendorProduct.getStock();

        if (productStock == 0) throw new CustomBadRequestException("product is ran out of stock");

        if (cartItemDto.getQuantity() > productStock)
            throw new CustomConflictException("quantity selected is greater than product stock");

        CartItem excistingCartItem = cartItemRepository.findByProductIdAndCartIdAndVendorProductId(
                product.getId() ,
                cart.getId() ,
                vendorProduct.getId()
        );

        if(excistingCartItem != null) throw new CustomBadRequestException("cart item is already exists");

        CartItem cartItem = CartItem.builder()
                .id(new CartItemEmbeddedId(
                        authenticatedUserId,
                        product.getId(),
                        vendorProduct.getId()
                ))
                .vendorProduct(vendorProduct)
                .cart(cart)
                .product(product)
                .quantity(cartItemDto.getQuantity())
                .build();

            cartItemRepository.save(cartItem);

    }

    @Override
    @Transactional
    public void removeFromCart(RemoveFromCartDto removeFromCartDto) {
        long authenticatedUserId = ((AuthenticatedUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();

            CartItem cartItem = cartItemRepository.findByProductIdAndCartIdAndVendorProductId(
                    removeFromCartDto.getProductId(),
                    authenticatedUserId,
                    removeFromCartDto.getVendorProductId()
            );
            if (cartItem == null) throw new CustomNotFoundException("unable to find cart item");
            cartItemRepository.delete(cartItem);

    }


    @Override
    @Transactional
    public void removeAllFromCart(long userId) {
            Cart cart = cartRepository.findByCustomerId(userId);
            if(cart == null) throw new CustomNotFoundException("cart is not found");
            cartItemRepository.deleteAllBYCartId(cart.getId());

    }

}
