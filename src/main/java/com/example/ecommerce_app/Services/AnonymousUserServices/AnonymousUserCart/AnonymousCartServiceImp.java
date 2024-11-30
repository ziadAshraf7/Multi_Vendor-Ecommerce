package com.example.ecommerce_app.Services.AnonymousUserServices.AnonymousUserCart;

import com.example.ecommerce_app.Dto.CartItem.*;
import com.example.ecommerce_app.Entity.Product;
import com.example.ecommerce_app.Entity.VendorProduct;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomConflictException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomNotFoundException;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Mapper.CartItemMapper;
import com.example.ecommerce_app.Redis.Session.SessionData.AnonymousUserSessionData;
import com.example.ecommerce_app.Redis.Session.SessionManagement.RedisSessionServiceImp;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
import com.example.ecommerce_app.Repositery.Product.ProductRepository;
import com.example.ecommerce_app.Repositery.Vendor_Product.VendorProductRepository;
import com.example.ecommerce_app.Services.Cart.CartService;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Redis.Session.SessionManagement.SessionService;
import com.example.ecommerce_app.Services.Vendor_Product.VendorProductService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private final VendorProductService vendor_product_service;

    private final CartItemMapper cartItemMapper;

    private final ProductRepository productRepository;

    private final VendorProductRepository vendorProductRepository;

    @Override
    public void addToCart(CartItemDto cartItemDto , String  sessionId) {
            AnonymousUserSessionData anonymousUserSessionData = (AnonymousUserSessionData) sessionService.getSessionData(sessionId);
            VendorProduct vendorProduct = vendorProductRepository.findById(cartItemDto.getVendorProductId())
                    .orElseThrow(() -> new CustomNotFoundException("Cannot get vendorProduct item"));
            cartItemDto.setProductId(vendorProduct.getProduct().getId());
            if(anonymousUserSessionData.getCartItem(cartItemDto.getProductId() , cartItemDto.getVendorProductId()) == null){
                anonymousUserSessionData.addToCartItems(cartItemDto);
                sessionService.addToSession(sessionId , anonymousUserSessionData);
            }else {
                throw new CustomConflictException("cart item is already exists");
            }
    }


    @Override
    public List<CartItemResponseDto> getCartItemsBySessionId(String sessionId , Pageable pageable) {
        try {
            AnonymousUserSessionData anonymousUserSessionData = (AnonymousUserSessionData) sessionService.getSessionData(sessionId);
            if(anonymousUserSessionData == null) return new ArrayList<>();
            List<CartItemDto> cartItemDtos = anonymousUserSessionData.getCartItems();

            List<Long> vendorProductsIds = new ArrayList<>(cartItemDtos.size());
            cartItemDtos.forEach((ci) -> {
                vendorProductsIds.add(ci.getVendorProductId());
            });

            Page<VendorProduct> vendorProductsPage = vendorProductRepository.findAllVendorProductById(vendorProductsIds , pageable);

            List<VendorProduct> vendorProducts = new ArrayList<>(vendorProductsPage.stream().toList());

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
          AnonymousUserSessionData anonymousUserSessionData = (AnonymousUserSessionData) sessionService.getSessionData(sessionId);
          anonymousUserSessionData.updateCartItemData(
                  sessionUserCartItemQuantityDto.getProductId(),
                  sessionUserCartItemQuantityDto.getVendorProductId(),
                  sessionUserCartItemQuantityDto.getQuantity()
          );
          sessionService.addToSession(sessionId , anonymousUserSessionData);
      }catch (CustomRuntimeException e){
          throw new CustomRuntimeException("Failed While Updating the Cart Item Quantity");
      }
    }

    @Override
    public void removeFromCart(RemoveFromCartDto removeFromCartDto, String sessionId) {
        try {
            AnonymousUserSessionData anonymousUserSessionData = (AnonymousUserSessionData) sessionService.getSessionData(sessionId);
            anonymousUserSessionData.removeFromCartItems(removeFromCartDto.getProductId() , removeFromCartDto.getVendorProductId());
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Deleting From Cart");
        }
    }

    @Override
    public void removeAllFromCart( String sessionId) {
        try {
            AnonymousUserSessionData anonymousUserSessionData = ((RedisSessionServiceImp) sessionService).getSessionData(sessionId);
            anonymousUserSessionData.setCartItems(List.of());
            sessionService.addToSession(sessionId , anonymousUserSessionData);
        }catch (CustomRuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Deleting From Cart");
        }
    }


}
