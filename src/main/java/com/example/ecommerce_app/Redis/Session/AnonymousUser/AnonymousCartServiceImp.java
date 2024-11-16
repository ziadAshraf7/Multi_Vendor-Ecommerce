package com.example.ecommerce_app.Redis.Session.AnonymousUser;

import com.example.ecommerce_app.Dto.CartItem.*;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.VendorProduct;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Mapper.CartItemMapper;
import com.example.ecommerce_app.Redis.Session.SessionManagement.UserCartSessionServiceImp;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Cart.CartService;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Redis.Session.SessionManagement.SessionService;
import com.example.ecommerce_app.Services.Vendor_Product.Vendor_Product_Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
@Data
public class AnonymousCartServiceImp implements AnonymousCartService{

    private static final Logger log = LoggerFactory.getLogger(AnonymousCartServiceImp.class);

    private final SessionService sessionService;

    private final CartItemRepository cartItemRepository;

    private final CartService cartService;

    private final ProductService productService;

    private final Vendor_Product_Service vendor_product_service;

    private final CartItemMapper cartItemMapper;

    private final ProductRepository productRepository;

    private final VendorProductRepository vendorProductRepository;

    @Override
    public void addToCart(CartItemDto cartItemDto , String  sessionId) {
        try {
            AnonymousUserCartData anonymousUserCartData = (AnonymousUserCartData) sessionService.getSessionData(sessionId);
            VendorProduct vendorProduct = vendorProductRepository.findById(cartItemDto.getVendorProductId())
                    .orElseThrow(() -> new CustomNotFoundException("Cannot get vendorProduct item"));
            cartItemDto.setProductId(vendorProduct.getProduct().getId());
            if(anonymousUserCartData.getCartItem(cartItemDto.getProductId() , cartItemDto.getVendorProductId()) == null){
                anonymousUserCartData.addToCartItems(cartItemDto);
                sessionService.addToSession(sessionId , anonymousUserCartData);
            }
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Adding Product to Cart ");
        }
    }


    @Override
    public List<CartItemResponseDto> getCartItemsBySessionId(String sessionId) {
        try {
            AnonymousUserCartData anonymousUserCartData = (AnonymousUserCartData) sessionService.getSessionData(sessionId);
            if(anonymousUserCartData == null) return new ArrayList<>();
            List<CartItemDto> cartItemDtos = anonymousUserCartData.getCartItems();

            List<Long> vendorProductsIds = new ArrayList<>(cartItemDtos.size());
            cartItemDtos.forEach((ci) -> {
                vendorProductsIds.add(ci.getVendorProductId());
            });

            List<VendorProduct> vendorProducts = vendorProductRepository.findAllById(vendorProductsIds);

            List<CartItemResponseDto> cartItemResponseDtoList = new ArrayList<>(cartItemDtos.size());

            vendorProducts.sort(Comparator.comparing(VendorProduct::getId));
            cartItemDtos.sort(Comparator.comparing(CartItemDto::getVendorProductId));

            for(int i = 0 ; i < vendorProducts.size() ; i++){
                VendorProduct vendorProduct = vendorProducts.get(i);
                CartItemDto cartItemDto = cartItemDtos.get(i);
                Product product = vendorProduct.getProduct();
                cartItemResponseDtoList.add(CartItemResponseDto
                        .builder()
                                .productName(product.getName())
                                .price(vendorProduct.getPrice())
                                .quantity(cartItemDto.getQuantity())
                                .title(product.getTitle())
                                .thumbNail(product.getThumbNail())
                                .vendorProductId(vendorProduct.getId())
                                .productId(product.getId())
                        .build());
            }

            return cartItemResponseDtoList;
       }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Retrieving Cart Products");
        }
    }

    @Override
    public void modifyCartItemQuantity(CartItemQuantityDto sessionUserCartItemQuantityDto, String sessionId) {
      try {
          AnonymousUserCartData anonymousUserCartData = (AnonymousUserCartData) sessionService.getSessionData(sessionId);
          anonymousUserCartData.updateCartItemData(
                  sessionUserCartItemQuantityDto.getProductId(),
                  sessionUserCartItemQuantityDto.getVendorProductId(),
                  sessionUserCartItemQuantityDto.getQuantity()
          );
          sessionService.addToSession(sessionId , anonymousUserCartData);
      }catch (CustomRuntimeException e){
          throw new CustomRuntimeException("Failed While Updating the Cart Item Quantity");
      }
    }

    @Override
    public void removeFromCart(RemoveFromCartDto removeFromCartDto, String sessionId) {
        try {
            AnonymousUserCartData anonymousUserCartData = (AnonymousUserCartData) sessionService.getSessionData(sessionId);
            anonymousUserCartData.removeFromCartItems(removeFromCartDto.getProductId() , removeFromCartDto.getVendorProductId());
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Deleting From Cart");
        }
    }

    @Override
    public void removeAllFromCart(RemoveFromCartDto removeFromCartDto, String sessionId) {
        try {
            AnonymousUserCartData anonymousUserCartData = ((UserCartSessionServiceImp) sessionService).getSessionData(sessionId);
            anonymousUserCartData.setCartItems(List.of());
            sessionService.addToSession(sessionId , anonymousUserCartData);
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Deleting From Cart");
        }
    }


}
