package com.example.ecommerce_app.Session.AnonymousUser;

import com.example.ecommerce_app.Dto.CartItem.*;
import com.example.ecommerce_app.Dto.CartItem.Session.SessionUserCartItemQuantityDto;
import com.example.ecommerce_app.Entity.Vendor_Product;
import com.example.ecommerce_app.Exceptions.Exceptions.CustomRuntimeException;
import com.example.ecommerce_app.Mapper.CartItemMapper;
import com.example.ecommerce_app.Repositery.CartItem.CartItemRepository;
import com.example.ecommerce_app.Services.Cart.CartService;
import com.example.ecommerce_app.Services.Product.ProductService;
import com.example.ecommerce_app.Session.Session_Management.SessionService;
import com.example.ecommerce_app.Session.Session_Management.UserCartSessionServiceImp;
import com.example.ecommerce_app.Services.Vendor_Product.Vendor_Product_Service;
import com.example.ecommerce_app.Utills.UtilsClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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


    @Override
    public void addToCart(CartItemDto cartItem , String  sessionId) {
        try {

            AnonymousUserCartData anonymousUserCartData = (AnonymousUserCartData) sessionService.getSessionData(sessionId);

            if(anonymousUserCartData.getCartItem(cartItem.getProductId()) == null){
                anonymousUserCartData.addToCartItems(cartItem);
            }

            sessionService.addToSession(sessionId , anonymousUserCartData);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Adding Product to Cart ");
        }

    }


    @Override
    public List<CartItemDto> getCartItemsBySessionId(String sessionId) {
        try {
            AnonymousUserCartData anonymousUserCartData = (AnonymousUserCartData) sessionService.getSessionData(sessionId);
            List<CartItemDto> sessionCartItems = anonymousUserCartData.getCartItems();

            for(CartItemDto sessionCartItem : sessionCartItems){

                Vendor_Product vendor_product = vendor_product_service.getByVendorIdAndProductId(
                        sessionCartItem.getVendorId() ,
                        sessionCartItem.getProductId()
                );

                // In case of Changed Price from the Owner Vendor
                double vendor_product_price = vendor_product.getPrice();
                double vendor_product_discount = vendor_product.getDiscount();
                if(vendor_product_price != sessionCartItem.getPrice()) sessionCartItem.setPrice(vendor_product_price);
                if(vendor_product_discount != sessionCartItem.getDiscount()) sessionCartItem.setDiscount(vendor_product_discount);

                double oldTotalPricePerProduct = UtilsClass.calcProductFinalPrice(
                        sessionCartItem.getPrice() , sessionCartItem.getDiscount())
                        *
                        sessionCartItem.getQuantity();

                double newOldTotalPricePerProduct = UtilsClass.calcProductFinalPrice(
                        vendor_product_price , vendor_product_discount) * sessionCartItem.getQuantity();


                double oldTotalCartPrice = anonymousUserCartData.getTotalCartPrice();

                double newTotalCartPrice = ( oldTotalCartPrice - oldTotalPricePerProduct ) + newOldTotalPricePerProduct;

                anonymousUserCartData.setTotalCartPrice(newTotalCartPrice);

            }

            sessionService.addToSession(sessionId , anonymousUserCartData);
            return anonymousUserCartData.getCartItems();
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Retrieving Cart Products");

        }

    }

    @Override
    public void modifyCartItemQuantity(SessionUserCartItemQuantityDto sessionUserCartItemQuantityDto, String sessionId) {
      try {
          AnonymousUserCartData anonymousUserCartData = (AnonymousUserCartData) sessionService.getSessionData(sessionId);
          AnonymousUserCartData anonymousUserCartData1 =  anonymousUserCartData.updateCartItemData(
                  sessionUserCartItemQuantityDto.getProductId(),
                  sessionUserCartItemQuantityDto.getQuantity() ,
                  sessionUserCartItemQuantityDto.getProductPrice()
          );
          sessionService.addToSession(sessionId , anonymousUserCartData);
      }catch (RuntimeException e){
          throw new CustomRuntimeException("Failed While Updating the Cart Item Quantity");
      }

    }

    @Override
    public void removeFromCart(RemoveFromCartDto removeFromCartDto, String sessionId) {
        try {
            AnonymousUserCartData anonymousUserCartData = (AnonymousUserCartData) sessionService.getSessionData(sessionId);
            anonymousUserCartData.removeFromCartItems(removeFromCartDto.getProductId());
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Deleting From Cart");
        }
    }

    @Override
    public void removeAllFromCart(RemoveFromCartDto removeFromCartDto, String sessionId) {
        try {
            AnonymousUserCartData anonymousUserCartData = ((UserCartSessionServiceImp) sessionService).getSessionData(sessionId);
            anonymousUserCartData.setCartItems(List.of());
            anonymousUserCartData.setTotalCartPrice(0);
            sessionService.addToSession(sessionId , anonymousUserCartData);
        }catch (RuntimeException e){
            log.error(e.getMessage());
            throw new CustomRuntimeException("Failed While Deleting From Cart");
        }

    }


}
