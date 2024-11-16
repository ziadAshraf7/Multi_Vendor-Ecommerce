package com.example.ecommerce_app.Services.linkingUserCartsManagement;


import com.example.ecommerce_app.Dto.CartItem.*;
import com.example.ecommerce_app.Entity.*;
import com.example.ecommerce_app.Entity.Embedded_Ids.CartItem_EmbeddedId;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Redis.Session.AnonymousUser.AnonymousUserCartData;
import com.example.ecommerce_app.Repositery.Cart.CartRepository;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Cart.CartService;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Redis.Session.SessionManagement.SessionService;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Data
@Slf4j
public class linkingUserCartsManagementServiceImp implements linkingUserCartsManagementService {

    private final CartItemRepository cartItemRepository;

    private final CartService cartService;

    private final ProductService productService;

    private final CartRepository cartRepository;

    private final VendorProductRepository vendorProductRepository;

    private SessionService sessionService;


    @Override
    @Transactional
    public void linkBetweenSessionCartAndUserCart(String sessionId, long userId) {

        List<CartItemDto> sessionCartItems = ((AnonymousUserCartData) sessionService.getSessionData(sessionId)).getCartItems();
        if (sessionCartItems == null) return;
        if (sessionCartItems.isEmpty()) return;

        Cart cart = cartRepository.findById(userId).orElseThrow(() -> new CustomNotFoundException("cart for user id " + userId + " is not found"));

        List<CartItemWithProductIdDTO> userCartItems = cartItemRepository.findCartItemsWithProductIdByCartId(userId);

        Set<Long> userCartProductIds = userCartItems.stream()
                .map((CartItemWithProductIdDTO::getProductId))
                .collect(Collectors.toSet());

        List<CartItemDto> newCartItemsDto = new ArrayList<>(sessionCartItems.size());
        List<Long> vendorProductsId = new ArrayList<>(sessionCartItems.size());
        for (CartItemDto sessionCartItem : sessionCartItems) {
            if (!userCartProductIds.isEmpty() && !userCartProductIds.contains(sessionCartItem.getProductId())) {
                newCartItemsDto.add(sessionCartItem);
                vendorProductsId.add(sessionCartItem.getVendorProductId());
            }
        }

        List<VendorProduct> vendorProductList = vendorProductRepository.findAllVendorProductById(vendorProductsId);


        vendorProductList.sort(Comparator.comparing(VendorProduct::getId));
        newCartItemsDto.sort(Comparator.comparing(CartItemDto::getVendorProductId));

        List<CartItem> newCartItemsEntities = new ArrayList<>(vendorProductList.size());
        for (int i = 0; i < vendorProductList.size(); i++) {
            VendorProduct vendorProduct = vendorProductList.get(i);
            CartItemDto cartItemDto = newCartItemsDto.get(i);

            Product product = vendorProduct.getProduct();
            newCartItemsEntities.add(CartItem.builder()
                    .id(new CartItem_EmbeddedId(
                            userId,
                            product.getId(),
                            vendorProduct.getId()
                    ))
                    .vendorProduct(vendorProduct)
                    .cart(cart)
                    .product(product)
                    .quantity(cartItemDto.getQuantity())
                    .build());
        }

        cartItemRepository.saveAll(newCartItemsEntities);
        sessionService.deleteSession(sessionId);
    }


}
